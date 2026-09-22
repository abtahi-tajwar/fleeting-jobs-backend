from pydantic import BaseModel, HttpUrl

from app.common.config.PydanticCamelCaseModel import PydanticCamelCaseModel


class PostingCount(PydanticCamelCaseModel):
    selector: str


class Field(PydanticCamelCaseModel):
    type: str
    selector: str
    attribute: str | None = None
    absolute: bool | None = None


class Listing(PydanticCamelCaseModel):
    container: str
    fields: dict[str, Field]


class JobDetails(PydanticCamelCaseModel):
    description: str


class Pagination(PydanticCamelCaseModel):
    type: str
    parameter: str | None = None
    start: int | None = None
    increment: int | None = None
    page_size: int | None = None
    additional_parameters: dict[str, str] | None = None


class ParserTemplate(PydanticCamelCaseModel):
    company: str
    version: int
    listing_url: HttpUrl
    posting_count: PostingCount | None = None
    listing: Listing
    job_details: JobDetails | None = None
    pagination: Pagination | None = None


class ScrapeJobRequest(PydanticCamelCaseModel):
    company_id: int
    listing_url: HttpUrl
    parser_template: ParserTemplate