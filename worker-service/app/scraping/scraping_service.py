from playwright.sync_api import sync_playwright, Playwright
from bs4 import BeautifulSoup

def navigate_to (url: str):
    with sync_playwright() as playwright:
        current_page = 0
        size = 10
        jobs = []
        result_count = 0
        total_pages = 0

        browser = playwright.chromium.launch(headless=False)
        page = browser.new_page()
        page.goto(url)
        page.wait_for_load_state("networkidle")
        initial_content = page.content()

        soup = BeautifulSoup(initial_content, 'lxml')
        
        result_count = int(soup.select_one('span.result-count').text.strip())
        total_pages = result_count / size
        jobs.extend(extract_listings(initial_content))
        for current_page in range(int(total_pages)):
            current_page += 1
            print(f"Currently parsing: {url}?form={current_page * size}&s=1")
            page.goto(f"{url}?form={current_page * size}&s=1")
            content = page.content()
            jobs.extend(extract_listings(content))
        browser.close()
        return jobs

def extract_listings(content: str):
    jobs = []
    soup = BeautifulSoup(content, 'lxml')
    job_listing_cards = soup.select('div.information')
    for card in job_listing_cards:
        job_title = card.select_one('span[role="heading"] > a > div > span').text.strip()
        job_url = card.select_one('span[role="heading"] > a')['href']
        jobs.append({
            "title": job_title,
            "url": job_url
        })

    return jobs 
