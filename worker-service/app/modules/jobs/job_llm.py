import json

from app.common.service.llm.ollama.ollama_service import ollama_service
from app.common.service.llm.openai.openai_service import openai_service


class JobLlm:
    async def extract_job_requirements(self, job_description: str):
        prompt = f"""
            You will be provided with a job description below, from where you will have to find out the required skills for this job
            The skills should be maximum 3 words long
            
            Divide the skills into 2 parts: 
            1 - Tools & Technologies: It will consist of the following things 
                a - Programming Languages
                b - Framework Libraries
                c - Tools & Platforms
                d - Technical Concepts
            2 - Soft Skills
            
            
            The skills will be going into resume, so make sure the skills are directly relevent to job description, also the skill keywords will competitively score high in ATS phase
            
            JOB_DESCRIPTION:
            {job_description} 
        """

        example_format = {
            "tools_technologies": ["skill1", "skill2"],
            "soft_skills": ["skill3", "skill4"]
        }
        # ollama_response = await ollama_service.json_response_chat(prompt, example_format)
        # print(json.dumps(ollama_response, indent=1))
        openai_response = await openai_service.json_response_chat(prompt, example_format)
        print(json.dumps(openai_response, indent=1))
        return openai_response

job_llm = JobLlm()