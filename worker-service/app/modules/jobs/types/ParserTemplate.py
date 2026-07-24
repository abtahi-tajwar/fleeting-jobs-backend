from pydantic import BaseModel, HttpUrl


class PostingCount(BaseModel):
    selector: str


class Field(BaseModel):
    type: str
    selector: str
    attribute: str | None = None
    absolute: bool | None = None


class Listing(BaseModel):
    container: str
    fields: dict[str, Field]


class JobDetails(BaseModel):
    description: str


class Pagination(BaseModel):
    type: str
    parameter: str | None = None
    start: int | None = None
    increment: int | None = None
    page_size: int | None = None
    additional_parameters: dict[str, str] | None = None


class ParserTemplate(BaseModel):
    company: str
    version: int
    start_url: HttpUrl
    posting_count: PostingCount | None = None
    listing: Listing
    job_details: JobDetails | None = None
    pagination: Pagination | None = None


class ScrapeJobRequest(BaseModel):
    company_id: int
    listing_url: HttpUrl
    parser_template: ParserTemplate