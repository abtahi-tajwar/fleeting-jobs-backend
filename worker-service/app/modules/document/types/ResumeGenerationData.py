from pydantic import BaseModel

class ResumeGenerationData(BaseModel):
    skills: ResumeGenerationDataSkills

class ResumeGenerationDataSkills(BaseModel):
    tools_technologies: list[str]
    soft_skills: list[str]