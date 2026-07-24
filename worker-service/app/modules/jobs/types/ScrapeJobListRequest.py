from pydantic import BaseModel, HttpUrl

from app.modules.jobs.types.ParserTemplate import ParserTemplate


class ScrapeJobListRequest(BaseModel):
    company_id: int
    listing_url: HttpUrl
    parser_template: ParserTemplate