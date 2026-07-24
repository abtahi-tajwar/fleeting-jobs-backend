import asyncio
import math
from playwright.async_api import async_playwright
from bs4 import BeautifulSoup

from app.common.rabbit.rabbit_service import rabbit_service
from app.common.rabbit.rabbit_config import RECEIVE_JOB_DETAILS_QUEUE, RECEIVE_NEW_JOB_LISTING
from app.modules.jobs.types.ParserTemplate import ParserTemplate


class JobParser:
    def __init__(self):
        self.playwright = None
        self.browser = None
        self.page = None

    async def start(self):
        self.playwright = await async_playwright().start()
        self.browser = await self.playwright.chromium.launch(headless=True)
        self.page = await self.browser.new_page()

    async def close(self):
        await self.browser.close()
        await self.playwright.stop()

    async def parse_jobs(self, company_id: int, config: ParserTemplate):
        size = 10
        jobs = []

        await self.page.goto(str(config.start_url))
        await self.page.wait_for_selector(config.listing.container)

        initial_content = await self.page.content()
        soup = BeautifulSoup(initial_content, "lxml")

        result_count = int(
            soup.select_one(config.posting_count.selector).text.strip()
        )

        total_pages = result_count / size

        first_page_extraction = await self.extract_listings(
            company_id,
            initial_content,
            config
        )
        jobs.extend(first_page_extraction)

        for current_page in range(1, math.ceil(total_pages)):
            page_url = (
                f"{config.start_url}?"
                f"{config.pagination.parameter}="
                f"{current_page * config.pagination.increment}&s=1"
            )

            print(f"Currently parsing: {page_url}")

            await self.page.goto(page_url)
            await self.page.wait_for_selector(config.listing.container)

            content = await self.page.content()
            extracted_jobs = await self.extract_listings(
                company_id,
                content,
                config
            )
            jobs.extend(extracted_jobs)

        return jobs

    async def extract_listings(
            self,
            company_id: int,
            content: str,
            config: ParserTemplate
    ):
        jobs = []
        soup = BeautifulSoup(content, "lxml")

        job_listing_cards = soup.select(config.listing.container)

        title_field = config.listing.fields["title"]
        url_field = config.listing.fields["url"]

        for card in job_listing_cards:
            job_title = card.select_one(
                title_field.selector
            ).text.strip()

            job_url = card.select_one(
                url_field.selector
            )[url_field.attribute]

            new_job = {
                "company_id": company_id,
                "title": job_title,
                "url": job_url
            }

            jobs.append(new_job)

            asyncio.create_task(
                rabbit_service.publish(RECEIVE_NEW_JOB_LISTING, new_job)
            )

        return jobs

    async def extract_job_details(
            self,
            job_id: int,
            url: str,
            config: ParserTemplate
    ):
        await self.page.goto(url)
        await self.page.wait_for_selector(config.job_details.description)

        content = await self.page.content()
        content_soup = BeautifulSoup(content, "lxml")

        description = content_soup.select_one(
            config.job_details.description
        ).text.strip()


job_parser = JobParser()
