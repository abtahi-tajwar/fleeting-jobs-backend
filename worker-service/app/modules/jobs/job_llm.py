import json

from app.common.service.llm.ollama.ollama_service import ollama_service
from app.common.service.llm.openai.openai_service import openai_service
from app.modules.document.types.ResumeGenerationData import ResumeGenerationData
from app.modules.profile.types.Profile import Profile


class JobLlm:

    async def extract_job_requirements(self, job_description: str):
        prompt = f"""
            You will be provided with a job description below, from where you will have to find out the required skills for this job.
            The skills should be maximum 3 words long.

            Divide the skills into 2 parts:
            1 - Tools & Technologies: It will consist of the following things
                a - Programming Languages
                b - Framework Libraries
                c - Tools & Platforms
                d - Technical Concepts
            2 - Soft Skills

            The skills will be going into resume, so make sure the skills are directly relevant to job description,
            also the skill keywords will competitively score high in ATS phase.

            Next thing you will find out is the responsibilities of this job, which will be used to generate resume summary,
            so make sure the responsibilities are directly relevant to job description.
            Make sure the responsibilities are very simple to understand.

            Then you will extract job title, first look if there is any job title mentioned in the job description,
            if not then you will have to find out the job title from the job description.

            JOB_DESCRIPTION:
            {job_description}
        """

        example_format = {
            "job_title": "Software Engineer",
            "skills": {
                "tools_technologies": ["skill1", "skill2"],
                "soft_skills": ["skill3", "skill4"],
            },
            "responsibilities": [
                "You will be doing X",
                "You will be doing Y",
            ],
        }

        # ollama_response = await ollama_service.json_response_chat(
        #     prompt,
        #     example_format,
        # )

        openai_response = await openai_service.json_response_chat(
            prompt,
            example_format,
        )

        print(json.dumps(openai_response, indent=1))

        return openai_response
    
    def model_list_to_json(self, models):
        return json.dumps([
            model.model_dump(mode="json")
            for model in models
        ])

    async def tailor_resume_data(
        self,
        profile: Profile,
        extracted_job_data: ResumeGenerationData,
    ):
        prompt = f"""
        You will be provided with extracted skill requirements from a job description
        and the user's existing profile.

        Your task is to create a completely tailored resume data object.

        First, analyze the job requirements and the user's existing skills.

        Find:
        1. Skills already present in the user's profile that are relevant to the job.
        2. Skills required by the job that are missing from the user's profile.
        3. Relevant experience from the user's work history that demonstrates the required skills.

        Only use information that can reasonably be supported by the user's existing
        profile. Do not invent companies, projects, technologies, responsibilities,
        education, certifications, or experience.

        Create a focused skills list containing approximately 5-10 highly relevant
        skills for this specific job.

        For work experience:
        - Keep the user's actual companies and positions.
        - Rewrite the descriptions to emphasize experience relevant to this job.
        - Do not invent experience.
        - Do not claim that the user performed work that is not supported by their
        existing experience.
        - Use concise, professional resume language.
        - Preserve the actual employment dates and other factual information.

        For education:
        - Keep the user's education information as-is.

        For awards:
        - Keep an award only if it is relevant or useful for this job.
        - Otherwise return an empty list.

        For certifications:
        - Keep the user's certifications as-is.
        - Do not invent certifications.

        ========================
        JOB INFORMATION
        ========================

        JOB_TITLE:
        {extracted_job_data["job_title"]}

        JOB_REQUIRED_SKILLS:
        {json.dumps(extracted_job_data["skills"], indent=2)}

        JOB_RESPONSIBILITIES:
        {json.dumps(extracted_job_data["responsibilities"], indent=2)}

        ========================
        USER PROFILE
        ========================

        USER_EXISTING_SKILLS:
        {self.model_list_to_json(profile.skills)}

        USER_WORK_EXPERIENCE:
        {self.model_list_to_json(profile.work_experiences)}

        USER_EDUCATION:
        {self.model_list_to_json(profile.educations)}

        USER_AWARDS:
        {self.model_list_to_json(profile.awards)}

        USER_CERTIFICATIONS:
        {self.model_list_to_json(profile.certifications)}

        ========================
        OUTPUT REQUIREMENTS
        ========================

        Return ONLY valid JSON.

        Do not include:
        - Markdown
        - ```json
        - Explanations
        - Comments
        - Additional fields

        The JSON must follow exactly the structure shown in the example below.

        The keys must not be renamed.
    """

        example_format = {
            "awards": [
                {
                    "id": 1,
                    "title": "Valedictorian Award",
                    "organization": "Niagara University in Ontario",
                    "date": "2026-06-01",
                    "description": (
                        "Awarded for the M.Sc. in Information Security & Digital "
                        "Forensics (CGPA 3.92 / 4.00)."
                    ),
                }
            ],
            "certifications": [],
            "educations": [
                {
                    "id": 1,
                    "institution": "Niagara University in Ontario",
                    "degree": "M.Sc.",
                    "major": "Information Security & Digital Forensics",
                    "gpa": "3.92 / 4.00",
                    "startDate": "2024-09-01",
                    "endDate": "2026-08-31",
                    "graduated": True,
                }
            ],
            "skills": [
                {
                    "id": 2,
                    "name": "Spring Boot",
                    "category": "Languages/Frameworks",
                    "strength": 9,
                    "yearsExperience": 3,
                    "notes": None,
                }
            ],
            "workExperiences": [
                {
                    "id": 1,
                    "company": "Fleeting Trails",
                    "position": "Software Developer & Founder",
                    "location": "Toronto, ON",
                    "employmentType": "Self-Employed",
                    "startDate": "2025-01-01",
                    "endDate": None,
                    "currentlyWorking": True,
                    "description": (
                        "Founded a software venture for client work that expanded "
                        "into product development."
                    ),
                }
            ],
        }

        # Add the JSON example without manually escaping hundreds of braces.
        prompt += f"""

        EXAMPLE OUTPUT STRUCTURE:

        {json.dumps(example_format, indent=2)}

        Remember: return ONLY valid JSON matching this structure.
        """

        openai_response = await openai_service.json_response_chat(
            prompt,
            example_format,
        )

        print(json.dumps(openai_response, indent=2))

        return openai_response
    
    
job_llm = JobLlm()