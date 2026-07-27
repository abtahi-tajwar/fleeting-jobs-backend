from app.modules.document.generate_resume_pdf import pdf_service
from app.modules.jobs.job_parser import job_parser


class DocumentService:
    async def generate_resume_from_url(self, url: str):
        description = await job_parser.extract_job_details_without_parser(url)
        print(description)
        pdf = self.generate_resume_from_description(description)
        return pdf
    def generate_resume_from_description(self, description: str):
        return pdf_service.generate_resume()

document_service = DocumentService()