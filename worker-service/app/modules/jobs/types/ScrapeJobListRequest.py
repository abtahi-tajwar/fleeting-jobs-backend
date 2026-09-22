from pydantic import BaseModel, HttpUrl

from app.modules.jobs.types.ParserTemplate import ParserTemplate
from app.common.config.PydanticCamelCaseModel import PydanticCamelCaseModel


class ScrapeJobListRequest(PydanticCamelCaseModel):
    company_id: int
    listing_url: HttpUrl
    parser_template: ParserTemplate