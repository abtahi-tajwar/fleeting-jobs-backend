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

    async def parse_jobs(self, url: str, config: ParserTemplate):
        current_page = 0
        size = 10
        jobs = []
        result_count = 0
        total_pages = 0

        await self.page.goto(config['start_url'])
        await self.page.wait_for_selector(config['listing']['container'])

        initial_content = await self.page.content()

        soup = BeautifulSoup(initial_content, 'lxml')

        result_count = int(
            soup.select_one(config['posting_count']['selector']).text.strip()
        )

        total_pages = result_count / size
        first_page_extraction = await self.extract_listings(initial_content, config)
        jobs.extend(first_page_extraction)

        for current_page in range(math.ceil(total_pages)):
            current_page += 1

            print(
                f"Currently parsing: "
                f"{config['start_url']}?"
                f"{config['pagination']['parameter']}={current_page * size}&s=1"
            )

            await self.page.goto(
                f"{config['start_url']}?{config['pagination']['parameter']}={current_page * size}&s=1"
            )

            await self.page.wait_for_selector(config['listing']['container'])

            content = await self.page.content()
            extracted_jobs = await self.extract_listings(content, config)
            jobs.extend(extracted_jobs)

        return jobs

    async def extract_listings(self, content: str, config: ParserTemplate):
        jobs = []

        soup = BeautifulSoup(content, "lxml")

        job_listing_cards = soup.select(config["listing"]["container"])

        for card in job_listing_cards:
            job_title = card.select_one(
                config["listing"]["fields"]["title"]["selector"]
            ).text.strip()

            job_url = card.select_one(
                config["listing"]["fields"]["url"]["selector"]
            )[config["listing"]["fields"]["url"]["attribute"]]

            new_job = {
                "title": job_title,
                "url": job_url
            }
            jobs.append(new_job)
            asyncio.create_task(
                rabbit_service.publish(RECEIVE_NEW_JOB_LISTING, new_job)
            )

        return jobs

    async def extract_job_details(self, job_id: int, url: str, config: dict):
        print(f"Extracting job details from: {url}")

        await self.page.goto(url)
        await self.page.wait_for_selector(config['job_details']['description'])
        content = await self.page.content()

        content_soup = BeautifulSoup(content, 'lxml')
        print(config['job_details']['description'])
        description = content_soup.select_one(config['job_details']['description']).text.strip()

        result = {
            "id": job_id,
            "url": url,
            "description": description
        }
        await rabbit_service.publish(RECEIVE_JOB_DETAILS_QUEUE, result)


job_parser = JobParser()
