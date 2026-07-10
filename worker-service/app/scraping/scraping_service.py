from playwright.sync_api import sync_playwright, Playwright
from bs4 import BeautifulSoup

def navigate_to (url: str):
    with sync_playwright() as playwright:
        browser = playwright.chromium.launch(headless=True)
        page = browser.new_page()
        page.goto(url)
        content = page.content()
        browser.close()
        return extract_listings(content)

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
