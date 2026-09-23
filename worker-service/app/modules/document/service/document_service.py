import json

from app.modules.document.generate_resume_pdf import pdf_service
from app.modules.document.service._temps.temp_data import (
    EXTRACTED_JOB_DATA,
    TAILORED_RESUME_DATA,
)
from app.modules.document.types.ResumeGenerationData import (
    ResumeGenerationData,
)
from app.modules.jobs.job_llm import job_llm
from app.modules.jobs.job_parser import job_parser
from app.modules.profile.types.Profile import Profile


class DocumentService:

    async def generate_resume_from_url(
        self,
        url: str,
        profile: Profile,
    ):
        description = await job_parser.extract_job_details_without_parser(
            url,
            profile,
        )

        return await self.generate_resume_from_description(
            description,
            profile,
        )

    async def generate_resume_from_description(
        self,
        description: str,
        profile: Profile,
    ):
        # ---------------------------------------------------------
        # Extract job requirements
        # ---------------------------------------------------------

        # TODO: Replace temporary data with LLM response.
        #
        # extracted_job_data = await job_llm.extract_job_requirements(
        #     description
        # )

        extracted_job_data = EXTRACTED_JOB_DATA

        print("Extracted job data:")
        print(
            json.dumps(
                extracted_job_data,
                indent=2,
            )
        )

        # ---------------------------------------------------------
        # Tailor profile to job
        # ---------------------------------------------------------

        # TODO: Replace temporary data with LLM response.
        #
        # tailored_resume_data = await job_llm.tailor_resume_data(
        #     profile,
        #     extracted_job_data,
        # )

        tailored_resume_data = TAILORED_RESUME_DATA

        print("Tailored resume data:")
        print(
            json.dumps(
                tailored_resume_data,
                indent=2,
            )
        )

        # ---------------------------------------------------------
        # Convert tailored JSON into Pydantic Profile
        # ---------------------------------------------------------

        tailored_profile = Profile.model_validate(
            tailored_resume_data
        )

        # ---------------------------------------------------------
        # Create final resume generation data
        # ---------------------------------------------------------

        final_data = ResumeGenerationData(
            job_title=extracted_job_data["job_title"],
            profile=tailored_profile,
        )

        print("Final resume data:")
        print(
            json.dumps(
                final_data.model_dump(mode="json"),
                indent=2,
            )
        )

        # ---------------------------------------------------------
        # Generate PDF
        # ---------------------------------------------------------

        return pdf_service.generate_resume(final_data)


document_service = DocumentService()