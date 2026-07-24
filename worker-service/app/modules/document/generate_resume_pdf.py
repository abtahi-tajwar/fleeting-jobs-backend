# app/modules/documents/pdf_service.py
from io import BytesIO

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import mm
from reportlab.platypus import (
    KeepTogether,
    ListFlowable,
    ListItem,
    Paragraph,
    SimpleDocTemplate,
    Spacer,
    Table,
    TableStyle,
)
from reportlab.platypus import HRFlowable

class PdfService:
    BLUE = "#1F5A94"
    DARK = "#1F2937"
    MUTED = "#4B5563"

    def generate_resume(self) -> BytesIO:
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

        # Header - equivalent to \makecvtitle
        story.append(Paragraph(
            '<font color="#666666">MD Abtahi</font> Tajwar',
            styles["name"],
        ))

        contact = (
            "+1 (437) 955 1053  |  "
            "abtahitajwar@gmail.com  |  "
            "abtahitajwar.com<br/>"
            "LinkedIn: linkedin.com/in/abtahi-tajwar  |  "
            "GitHub: github.com/abtahi-tajwar  |  "
            "X: @abtahi_t"
        )
        story.append(Paragraph(contact, styles["contact"]))
        story.append(Spacer(1, 6 * mm))

        # Technical Skills
        story.append(self._section("Technical Skills", styles))
        story.append(self._label_value(
            "Tools &amp; Technologies",
            "Java, Python, JavaScript, Spring Boot, React, Next.js, "
            "SvelteKit, ASP.NET, PostgreSQL, RabbitMQ, Docker, Redis, "
            "AI Engineering, Prompt Engineering, LLM Integration, "
            "Playwright, Git",
            styles,
        ))
        story.append(self._label_value(
            "Soft Skills",
            "Communication, Problem Solving, Critical Analysis, Curiosity, "
            "Collaboration, Adaptability, Detail Oriented",
            styles,
        ))

        # Experience
        story.append(self._section("Experience", styles))

        story.extend(self._experience(
            date="Jan 2025 - Present",
            title="Founder &amp; Software Engineer",
            company="Fleeting Trails",
            location="Remote",
            bullets=[
                "Built <b>VaneJS</b>, a lightweight JavaScript templating "
                "library for reactive web applications.",
                "Developed <b>SymamLaw</b>, a cross-platform legal education "
                "platform using React Native, Next.js, Spring Boot, and PostgreSQL.",
                "Architected scalable backend systems using Spring Boot, Python, "
                "RabbitMQ, PostgreSQL, Docker, and LLM integration.",
                "Building <b>Fleeting Jobs</b>, an AI-assisted career platform "
                "for job discovery and personalized applications.",
            ],
            styles=styles,
        ))

        story.extend(self._experience(
            date="Apr 2022 - Nov 2024",
            title="Full-Stack Developer",
            company="Alagzoo Software LLC",
            location="Remote / Chicago, US",
            bullets=[
                "Led frontend architecture and contributed to backend development "
                "for <b>Bleaum</b>, a multi-tenant retail and fintech platform "
                "adopted by 100+ vendors.",
                "Re-engineered POS, analytics, and checkout with React, Next.js, "
                "Node.js, and PostgreSQL - achieved 40% faster loads and 99.9% uptime.",
                "Mentored juniors, coordinated sprints, and maintained CI/CD "
                "with Docker and AWS.",
            ],
            styles=styles,
        ))

        story.extend(self._experience(
            date="May 2021 - Apr 2022",
            title="Web Developer",
            company="Xit Bangladesh",
            location="Dhaka, BD",
            bullets=[
                "Built and maintained production e-commerce platforms in React and Laravel.",
                "Developed a Resume Builder web app with live preview and Firebase backend.",
                "Collaborated with design and QA to ensure accessibility and responsive UI.",
                (
                    'Contributed to the <a href="https://github.com/n0k1b/e-exam">'
                    '<font color="#1F5A94"><u>e-exam</u></font></a> platform supporting '
                    "secure assessments, question management, and automated result processing."
                ),
            ],
            styles=styles,
        ))

        # Projects
        story.append(self._section("Selected Projects", styles))

        story.append(self._project(
            "VaneJS",
            "Engineered a lightweight reactive JavaScript UI library with "
            "declarative bindings, fine-grained DOM updates, and compiler-free "
            "templating using tagged literals. Built entirely without external "
            "dependencies or build tools, demonstrating expertise in frontend "
            "architecture, reactivity systems, and framework design.<br/>"
            '<b>Link:</b> <a href="https://vanejs.netlify.app/">'
            '<font color="#1F5A94">https://vanejs.netlify.app/</font></a><br/>'
            '<b>Details:</b> <a href="https://abtahitajwar.com/project/?id=vanejs">'
            '<font color="#1F5A94">abtahitajwar.com/project/?id=vanejs</font></a>',
            styles,
        ))

        story.append(self._project(
            "Fleeting Jobs",
            "Developing an AI-assisted career platform for job discovery and "
            "personalized applications. Designed a scalable architecture with "
            "Spring Boot, Python, RabbitMQ, Playwright, PostgreSQL, Docker, "
            "and LLM integration.<br/>"
            '<b>Live:</b> <a href="https://fleetingjobs.netlify.app/">'
            '<font color="#1F5A94">fleetingjobs.netlify.app</font></a><br/>'
            '<b>Details:</b> <a href="https://abtahitajwar.com/project/fleeting-jobs/">'
            '<font color="#1F5A94">abtahitajwar.com/project/fleeting-jobs</font></a>',
            styles,
        ))

        story.append(self._project(
            "Bleaum",
            "Led development of a scalable e-commerce and fintech platform "
            "supporting real-time inventory, integrated payments, and "
            "multi-warehouse operations. Designed service-oriented backend "
            "infrastructure with PostgreSQL, Redis, Docker, and AWS to "
            "improve reliability, performance, and deployment automation.<br/>"
            '<b>Link:</b> <a href="https://www.bleaum.io/">'
            '<font color="#1F5A94">https://www.bleaum.io/</font></a><br/>'
            '<b>Details:</b> <a href="https://abtahitajwar.com/project/?id=bleaum">'
            '<font color="#1F5A94">abtahitajwar.com/project/?id=bleaum</font></a>',
            styles,
        ))

        # Education
        story.append(self._section("Education", styles))

        story.append(self._education(
            "2026",
            "M.Sc. in Information Security &amp; Digital Forensics",
            "Niagara University in Ontario - Toronto, ON",
            "CGPA: 3.92 / 4.00 (Received Valedictorian Award)",
            styles,
        ))

        story.append(self._education(
            "2022",
            "B.Sc. in Computer Science &amp; Engineering",
            "American International University Bangladesh - Dhaka, BD",
            "CGPA: 3.89 / 4.00",
            styles,
        ))

        document.build(story)
        buffer.seek(0)

        return buffer

    def _create_styles(self):
        styles = getSampleStyleSheet()

        styles.add(ParagraphStyle(
            name="name",
            parent=styles["Normal"],
            fontName="Helvetica",
            fontSize=22,
            leading=25,
            alignment=TA_CENTER,
            textColor=colors.black,
            spaceAfter=1 * mm,
        ))

        styles.add(ParagraphStyle(
            name="contact",
            parent=styles["Normal"],
            fontName="Helvetica",
            fontSize=8.5,
            leading=10.5,
            alignment=TA_CENTER,
            textColor=colors.black,
            spaceAfter=4 * mm,
        ))

        styles.add(ParagraphStyle(
            name="section",
            parent=styles["Normal"],
            fontName="Helvetica-Bold",
            fontSize=13,
            leading=15,
            textColor=colors.black,
            spaceBefore=2.5 * mm,
            spaceAfter=0.5 * mm,
        ))

        styles.add(ParagraphStyle(
            name="normal_text",
            parent=styles["Normal"],
            fontName="Helvetica",
            fontSize=8.5,
            leading=10.4,
            textColor=colors.black,
        ))

        styles.add(ParagraphStyle(
            name="entry_title",
            parent=styles["normal_text"],
            fontName="Helvetica-Bold",
        ))

        styles.add(ParagraphStyle(
            name="entry_role",
            parent=styles["normal_text"],
            fontName="Helvetica-Oblique",
        ))

        styles.add(ParagraphStyle(
            name="entry_date",
            parent=styles["normal_text"],
            fontName="Helvetica-Bold",
            textColor=colors.HexColor(self.MUTED),
        ))

        return styles

    def _section(self, title: str, styles):
        return KeepTogether([
            Paragraph(title, styles["section"]),
            HRFlowable(
                width="100%",
                thickness=0.5,
                color=colors.black,
                spaceAfter=2 * mm,
            ),
        ])

    def _label_value(self, label: str, value: str, styles):
        return Paragraph(f"<b>{label}:</b> {value}", styles["normal_text"])

    def _experience(self, date, title, company, location, bullets, styles):
        company_line = Table(
            [[
                Paragraph(company, styles["entry_title"]),
                Paragraph(f"<b>{location}</b>", styles["normal_text"]),
            ]],
            colWidths=[125 * mm, 45 * mm],
        )

        role_line = Table(
            [[
                Paragraph(title, styles["entry_role"]),
                Paragraph(f"<i>{date}</i>", styles["normal_text"]),
            ]],
            colWidths=[125 * mm, 45 * mm],
        )

        for table in [company_line, role_line]:
            table.setStyle(TableStyle([
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
                ("ALIGN", (1, 0), (1, 0), "RIGHT"),
                ("LEFTPADDING", (0, 0), (-1, -1), 0),
                ("RIGHTPADDING", (0, 0), (-1, -1), 0),
                ("TOPPADDING", (0, 0), (-1, -1), 0),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 0),
            ]))

        bullet_list = ListFlowable(
            [
                ListItem(Paragraph(bullet, styles["normal_text"]))
                for bullet in bullets
            ],
            bulletType="bullet",
            leftIndent=5 * mm,
            bulletIndent=1.5 * mm,
            bulletFontSize=5,
            spaceAfter=1.5 * mm,
        )

        # Do not wrap this in KeepTogether.
        return [
            company_line,
            role_line,
            bullet_list,
        ]

    def _project(self, title: str, description: str, styles):
        return Paragraph(
            f'<b>{title}:</b> {description}',
            ParagraphStyle(
                "project",
                parent=styles["normal_text"],
                spaceAfter=1.2 * mm,
            ),
        )

    def _education(self, year, degree, school_location, details, styles):
        table = Table(
            [[
                Paragraph(year, styles["entry_date"]),
                Paragraph(
                    f"<b>{degree}</b><br/>"
                    f"{school_location}<br/>"
                    f'<font color="{self.MUTED}">{details}</font>',
                    styles["normal_text"],
                ),
            ]],
            colWidths=[34 * mm, 136 * mm],
        )

        table.setStyle(TableStyle([
            ("VALIGN", (0, 0), (-1, -1), "TOP"),
            ("LEFTPADDING", (0, 0), (-1, -1), 0),
            ("RIGHTPADDING", (0, 0), (-1, -1), 0),
            ("TOPPADDING", (0, 0), (-1, -1), 0),
            ("BOTTOMPADDING", (0, 0), (-1, -1), 2 * mm),
        ]))

        return table


pdf_service = PdfService()