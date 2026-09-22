import asyncio

from fastapi import FastAPI
from contextlib import asynccontextmanager

from starlette.responses import StreamingResponse

from app.common.rabbit.rabbit_config import REQUEST_JOB_DETAILS_QUEUE
from app.common.rabbit.rabbit_service import rabbit_service
from app.modules.document.service.document_service import document_service
from app.modules.document.types.GenerateResumeFromUrl import GenerateResumeFromUrl, GenerateResumeFromDescription
from app.modules.jobs.jobs_callback import job_details_fetch_callback
from app.modules.jobs.job_parser import job_parser
from app.modules.document.generate_resume_pdf import pdf_service

from app.modules.jobs.types.ParserTemplate import ScrapeJobRequest


async def start_consumer():
    await rabbit_service.connect()
    await rabbit_service.consume(
        REQUEST_JOB_DETAILS_QUEUE,
        job_details_fetch_callback
    )

@asynccontextmanager
async def lifespan(app: FastAPI):
    await job_parser.start()
    await start_consumer()
    print("RabbitMQ consumer started")
    yield
    print("Stopping RabbitMQ consumer")
    await rabbit_service.stop()

app = FastAPI(lifespan=lifespan)

@app.get("/")
def root():
    return {"message": "Application is running successfully!"}


@app.post("/jobs/scrape-list/")
async def scrape_job_list(request: ScrapeJobRequest):
    template = request.parser_template
    url = request.listing_url
    company_id = request.company_id

    print(f"{company_id} scraped {url}")
    print(f"{template}")
    asyncio.create_task(
        job_parser.parse_jobs(company_id, template)
    )
    return 0

@app.post("/documents/generate/from_url/resume.pdf")
async def route_generate_resume_from_url(request: GenerateResumeFromUrl):
    url = request.url

    pdf_buffer = await document_service.generate_resume_from_url(url, request.profile)
    return StreamingResponse(
        pdf_buffer,
        media_type="application/pdf",
        headers={
            "Content-Disposition": 'attachment; filename="resume.pdf"'
        }
    )

@app.post("/documents/generate/from_description/resume.pdf")
async def route_generate_resume_from_description(request: GenerateResumeFromDescription):
    desc = request.description
    profile = request.profile

    pdf_buffer = await document_service.generate_resume_from_description(desc, profile)
    return StreamingResponse(
        pdf_buffer,
        media_type="application/pdf",
        headers={
            "Content-Disposition": 'attachment; filename="resume.pdf"'
        }
    )
    

