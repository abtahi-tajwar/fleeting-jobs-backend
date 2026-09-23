from pydantic import BaseModel
from app.modules.profile.types.Profile import Profile

# class ResumeGenerationData(BaseModel):
#     job_title: str
#     skills: ResumeGenerationDataSkills
#     responsibilities: list[str]
    
class ResumeGenerationData(BaseModel):
    job_title: str
    profile: Profile

class ResumeGenerationDataSkills(BaseModel):
    tools_technologies: list[str]
    soft_skills: list[str]
    