from playwright.sync_api import sync_playwright, Playwright
from bs4 import BeautifulSoup

def parse_jobs (url: str, config: dict):
    with sync_playwright() as playwright:
        current_page = 0
        size = 10
        jobs = []
        result_count = 0
        total_pages = 0

        browser = playwright.chromium.launch(headless=False)
        page = browser.new_page()
        page.goto(config['start_url'])
        page.wait_for_selector(config['listing']['container'])
        initial_content = page.content()

        soup = BeautifulSoup(initial_content, 'lxml')
        
        result_count = int(soup.select_one(config['posting_count']['selector']).text.strip())
        total_pages = result_count / size
        jobs.extend(extract_listings(initial_content, config))
        
        for current_page in range(int(total_pages)):
            current_page += 1
            print(f"Currently parsing: {config['start_url']}?{config['pagination']['parameter']}={current_page * size}&s=1")
            page.goto(f"{config['start_url']}?from={current_page * size}&s=1")
            page.wait_for_selector(config['listing']['container'])
            content = page.content()
            jobs.extend(extract_listings(content, config))
        browser.close()
        return jobs

def extract_listings(content: str, config: dict):
    jobs = []
    soup = BeautifulSoup(content, 'lxml')
    # job_listing_cards = soup.select('div.information')
    # for card in job_listing_cards:
    #     job_title = card.select_one('span[role="heading"] > a > div > span').text.strip()
    #     job_url = card.select_one('span[role="heading"] > a')['href']
    #     jobs.append({
    #         "title": job_title,
    #         "url": job_url
    #     })
    job_listing_cards = soup.select(config['listing']['container'])
    for card in job_listing_cards:
        job_title = card.select_one(config['listing']['fields']['title']['selector']).text.strip()
        job_url = card.select_one(config['listing']['fields']['url']['selector'])[config['listing']['fields']['url']['attribute']]
        jobs.append({
            "title": job_title,
            "url": job_url
        })

    return jobs 
