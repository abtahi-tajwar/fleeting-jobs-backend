# app/modules/document/generate_resume_pdf.py

from io import BytesIO

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import mm
from reportlab.platypus import (
    HRFlowable,
    KeepTogether,
    ListFlowable,
    ListItem,
    Paragraph,
    SimpleDocTemplate,
    Spacer,
    Table,
    TableStyle,
)

from app.modules.document.types.ResumeGenerationData import ResumeGenerationData


class PdfService:
    BLUE = "#1F5A94"
    DARK = "#1F2937"
    MUTED = "#4B5563"

    def generate_resume(self, data: ResumeGenerationData) -> BytesIO:
        buffer = BytesIO()

        document = SimpleDocTemplate(
            buffer,
            pagesize=A4,
            leftMargin=20 * mm,
            rightMargin=20 * mm,
            topMargin=16 * mm,
            bottomMargin=16 * mm,
        )

        styles = self._create_styles()
        story = []

        profile = data.profile

        # ============================================================
        # HEADER
        # ============================================================

        story.append(
            Paragraph(
                '<font color="#666666">MD Abtahi</font> Tajwar',
                styles["name"],
            )
        )

        contact = (
            "+1 (437) 955 1053  |  "
            "abtahitajwar@gmail.com  |  "
            "abtahitajwar.com<br/>"
            "LinkedIn: linkedin.com/in/abtahi-tajwar  |  "
            "GitHub: github.com/abtahi-tajwar  |  "
            "X: @abtahi_t"
        )

        story.append(
            Paragraph(
                contact,
                styles["contact"],
            )
        )

        story.append(Spacer(1, 6 * mm))

        # ============================================================
        # JOB TITLE
        # ============================================================

        if data.job_title:
            story.append(
                Paragraph(
                    data.job_title,
                    styles["target_job"],
                )
            )

            story.append(Spacer(1, 2 * mm))

        # ============================================================
        # TECHNICAL SKILLS
        # ============================================================

        if profile.skills:
            story.append(
                self._section(
                    "Technical Skills",
                    styles,
                )
            )

            # Group skills by category
            skills_by_category = {}

            for skill in profile.skills:
                category = skill.category or "Technical Skills"

                if category not in skills_by_category:
                    skills_by_category[category] = []

                skills_by_category[category].append(skill.name)

            for category, skills in skills_by_category.items():
                story.append(
                    self._label_value(
                        category,
                        ", ".join(skills),
                        styles,
                    )
                )

        # ============================================================
        # EXPERIENCE
        # ============================================================

        if profile.work_experiences:
            story.append(
                self._section(
                    "Experience",
                    styles,
                )
            )

            for experience in profile.work_experiences:
                story.extend(
                    self._experience(
                        experience=experience,
                        styles=styles,
                    )
                )

        # ============================================================
        # EDUCATION
        # ============================================================

        if profile.educations:
            story.append(
                self._section(
                    "Education",
                    styles,
                )
            )

            for education in profile.educations:
                story.append(
                    self._education(
                        education=education,
                        styles=styles,
                    )
                )

        # ============================================================
        # AWARDS
        # ============================================================

        if profile.awards:
            story.append(
                self._section(
                    "Awards",
                    styles,
                )
            )

            for award in profile.awards:
                story.append(
                    self._award(
                        award=award,
                        styles=styles,
                    )
                )

        # ============================================================
        # CERTIFICATIONS
        # ============================================================

        if profile.certifications:
            story.append(
                self._section(
                    "Certifications",
                    styles,
                )
            )

            for certification in profile.certifications:
                story.append(
                    self._certification(
                        certification=certification,
                        styles=styles,
                    )
                )

        document.build(story)

        buffer.seek(0)

        return buffer

    # ================================================================
    # STYLES
    # ================================================================

    def _create_styles(self):
        styles = getSampleStyleSheet()

        styles.add(
            ParagraphStyle(
                name="name",
                parent=styles["Normal"],
                fontName="Helvetica",
                fontSize=22,
                leading=25,
                alignment=TA_CENTER,
                textColor=colors.black,
                spaceAfter=1 * mm,
            )
        )

        styles.add(
            ParagraphStyle(
                name="contact",
                parent=styles["Normal"],
                fontName="Helvetica",
                fontSize=8.5,
                leading=10.5,
                alignment=TA_CENTER,
                textColor=colors.black,
                spaceAfter=4 * mm,
            )
        )

        styles.add(
            ParagraphStyle(
                name="target_job",
                parent=styles["Normal"],
                fontName="Helvetica-Bold",
                fontSize=10,
                leading=12,
                alignment=TA_CENTER,
                textColor=colors.HexColor(self.BLUE),
                spaceAfter=1 * mm,
            )
        )

        styles.add(
            ParagraphStyle(
                name="section",
                parent=styles["Normal"],
                fontName="Helvetica-Bold",
                fontSize=13,
                leading=15,
                textColor=colors.black,
                spaceBefore=2.5 * mm,
                spaceAfter=0.5 * mm,
            )
        )

        styles.add(
            ParagraphStyle(
                name="normal_text",
                parent=styles["Normal"],
                fontName="Helvetica",
                fontSize=8.5,
                leading=10.4,
                textColor=colors.black,
            )
        )

        styles.add(
            ParagraphStyle(
                name="entry_title",
                parent=styles["normal_text"],
                fontName="Helvetica-Bold",
            )
        )

        styles.add(
            ParagraphStyle(
                name="entry_role",
                parent=styles["normal_text"],
                fontName="Helvetica-Oblique",
            )
        )

        styles.add(
            ParagraphStyle(
                name="entry_date",
                parent=styles["normal_text"],
                fontName="Helvetica-Bold",
                textColor=colors.HexColor(self.MUTED),
            )
        )

        styles.add(
            ParagraphStyle(
                name="award",
                parent=styles["normal_text"],
                spaceAfter=2 * mm,
            )
        )

        styles.add(
            ParagraphStyle(
                name="certification",
                parent=styles["normal_text"],
                spaceAfter=2 * mm,
            )
        )

        return styles

    # ================================================================
    # SECTION
    # ================================================================

    def _section(self, title: str, styles):
        return KeepTogether(
            [
                Paragraph(
                    title,
                    styles["section"],
                ),
                HRFlowable(
                    width="100%",
                    thickness=0.5,
                    color=colors.black,
                    spaceAfter=2 * mm,
                ),
            ]
        )

    # ================================================================
    # LABEL / VALUE
    # ================================================================

    def _label_value(
        self,
        label: str,
        value: str,
        styles,
    ):
        return Paragraph(
            f"<b>{label}:</b> {value}",
            styles["normal_text"],
        )

    # ================================================================
    # EXPERIENCE
    # ================================================================

    def _experience(
        self,
        experience,
        styles,
    ):
        location = experience.location or ""

        company_line = Table(
            [
                [
                    Paragraph(
                        self._escape(experience.company),
                        styles["entry_title"],
                    ),
                    Paragraph(
                        f"<b>{self._escape(location)}</b>",
                        styles["normal_text"],
                    ),
                ]
            ],
            colWidths=[125 * mm, 45 * mm],
        )

        date = self._format_date_range(
            experience.start_date,
            experience.end_date,
            experience.currently_working,
        )

        role_line = Table(
            [
                [
                    Paragraph(
                        self._escape(experience.position),
                        styles["entry_role"],
                    ),
                    Paragraph(
                        f"<i>{date}</i>",
                        styles["normal_text"],
                    ),
                ]
            ],
            colWidths=[125 * mm, 45 * mm],
        )

        for table in [company_line, role_line]:
            table.setStyle(
                TableStyle(
                    [
                        (
                            "VALIGN",
                            (0, 0),
                            (-1, -1),
                            "TOP",
                        ),
                        (
                            "ALIGN",
                            (1, 0),
                            (1, 0),
                            "RIGHT",
                        ),
                        (
                            "LEFTPADDING",
                            (0, 0),
                            (-1, -1),
                            0,
                        ),
                        (
                            "RIGHTPADDING",
                            (0, 0),
                            (-1, -1),
                            0,
                        ),
                        (
                            "TOPPADDING",
                            (0, 0),
                            (-1, -1),
                            0,
                        ),
                        (
                            "BOTTOMPADDING",
                            (0, 0),
                            (-1, -1),
                            0,
                        ),
                    ]
                )
            )

        bullets = self._description_to_bullets(
            experience.description
        )

        bullet_list = ListFlowable(
            [
                ListItem(
                    Paragraph(
                        bullet,
                        styles["normal_text"],
                    )
                )
                for bullet in bullets
            ],
            bulletType="bullet",
            leftIndent=5 * mm,
            bulletIndent=1.5 * mm,
            bulletFontSize=5,
            spaceAfter=2 * mm,
        )

        return [
            company_line,
            role_line,
            bullet_list,
        ]

    # ================================================================
    # EDUCATION
    # ================================================================

    def _education(
        self,
        education,
        styles,
    ):
        year = (
            education.end_date.year
            if education.end_date
            else education.start_date.year
        )

        details = []

        if education.gpa:
            details.append(
                f"CGPA: {self._escape(education.gpa)}"
            )

        if education.graduated:
            details.append("Graduated")

        details_text = " | ".join(details)

        content = (
            f"<b>{self._escape(education.degree)}"
            f" in {self._escape(education.major)}</b><br/>"
            f"{self._escape(education.institution)}"
        )

        if details_text:
            content += (
                f'<br/><font color="{self.MUTED}">'
                f"{details_text}"
                "</font>"
            )

        table = Table(
            [
                [
                    Paragraph(
                        str(year),
                        styles["entry_date"],
                    ),
                    Paragraph(
                        content,
                        styles["normal_text"],
                    ),
                ]
            ],
            colWidths=[34 * mm, 136 * mm],
        )

        table.setStyle(
            TableStyle(
                [
                    (
                        "VALIGN",
                        (0, 0),
                        (-1, -1),
                        "TOP",
                    ),
                    (
                        "LEFTPADDING",
                        (0, 0),
                        (-1, -1),
                        0,
                    ),
                    (
                        "RIGHTPADDING",
                        (0, 0),
                        (-1, -1),
                        0,
                    ),
                    (
                        "TOPPADDING",
                        (0, 0),
                        (-1, -1),
                        0,
                    ),
                    (
                        "BOTTOMPADDING",
                        (0, 0),
                        (-1, -1),
                        2 * mm,
                    ),
                ]
            )
        )

        return table

    # ================================================================
    # AWARDS
    # ================================================================

    def _award(
        self,
        award,
        styles,
    ):
        return Paragraph(
            f"<b>{self._escape(award.title)}</b> | "
            f"{self._escape(award.organization)} "
            f"({award.date.strftime('%B %Y')})"
            f"<br/>{self._escape(award.description)}",
            styles["award"],
        )

    # ================================================================
    # CERTIFICATIONS
    # ================================================================

    def _certification(
        self,
        certification,
        styles,
    ):
        title = certification.name or "Certification"

        content = (
            f"<b>{self._escape(title)}</b>"
        )

        if certification.organization:
            content += (
                f" | {self._escape(certification.organization)}"
            )

        if certification.issue_date:
            content += (
                f" ({certification.issue_date.strftime('%B %Y')})"
            )

        if certification.description:
            content += (
                f"<br/>{self._escape(certification.description)}"
            )

        if certification.credential_url:
            content += (
                f'<br/><a href="{self._escape(certification.credential_url)}">'
                f'<font color="{self.BLUE}">'
                f"{self._escape(certification.credential_url)}"
                "</font></a>"
            )

        return Paragraph(
            content,
            styles["certification"],
        )

    # ================================================================
    # HELPERS
    # ================================================================

    def _format_date_range(
        self,
        start_date,
        end_date,
        currently_working: bool,
    ):
        start = start_date.strftime("%b %Y")

        if currently_working:
            return f"{start} - Present"

        if end_date:
            return f"{start} - {end_date.strftime('%b %Y')}"

        return start

    def _description_to_bullets(
        self,
        description: str,
    ):
        """
        Converts the newline-separated description stored in
        WorkExperience into individual resume bullets.
        """

        if not description:
            return []

        return [
            self._format_description_line(line.strip())
            for line in description.splitlines()
            if line.strip()
        ]

    def _format_description_line(
        self,
        text: str,
    ):
        """
        Escape ReportLab-sensitive characters while preserving
        simple existing HTML formatting if you want to add it later.

        For now, escape &, < and > so normal profile text doesn't
        accidentally break ReportLab's XML parser.
        """

        return (
            text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        )

    def _escape(self, text):
        if text is None:
            return ""

        return (
            str(text)
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        )


pdf_service = PdfService()