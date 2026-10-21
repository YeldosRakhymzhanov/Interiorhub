
const translations = {
    en: {
        navHome: "Home",
        navPortfolio: "Portfolio",
        navServices: "Services",
        navContact: "Contact",
        contactButton: "Let's talk ↗",
        eyebrow: "INTERIOR DESIGN STUDIO",
        heroTitle: 'Spaces that feel<br>like <em>you.</em>',
        heroDescription:
            "Thoughtful interiors, natural textures, and timeless spaces designed around the way you live.",
        exploreWork: "Explore our work",
        heroNote: "RESIDENTIAL · COMMERCIAL · BESPOKE",
        imageCaption: "01 / A place to feel at home",
        approach: "OUR APPROACH",
        introTitle:
            'Good design is not just how a space looks.<br>It\'s how it <em>makes you feel.</em>',
        introDescription:
            "We create considered interiors that bring together beauty, comfort, and everyday functionality.",
        selectedWork: "SELECTED WORK",
        portfolioTitle: "Spaces we've shaped.",
        startProject: "Start a project ↗",
        contactEyebrow: "HAVE A PROJECT IN MIND?",
        contactTitle: "Let's create something<br><em>meaningful.</em>",
        whatsappButton: "Chat on WhatsApp",
        footerTagline: "Thoughtful spaces. Timeless living.",
        noProjects: "Projects will be added soon.",
        loadError: "Unable to load projects. Please try again later.",
        projectFallback: "Interior design"
    },
    ru: {
        navHome: "Главная",
        navPortfolio: "Портфолио",
        navServices: "Услуги",
        navContact: "Контакты",
        contactButton: "Связаться ↗",
        eyebrow: "СТУДИЯ ИНТЕРЬЕРНОГО ДИЗАЙНА",
        heroTitle: 'Пространство,<br>которое <em>отражает вас.</em>',
        heroDescription:
            "Продуманные интерьеры, природные текстуры и вневременной дизайн для вашей жизни.",
        exploreWork: "Смотреть проекты",
        heroNote: "ЖИЛЫЕ · КОММЕРЧЕСКИЕ · ИНДИВИДУАЛЬНЫЕ",
        imageCaption: "01 / Место, где уютно",
        approach: "НАШ ПОДХОД",
        introTitle:
            'Хороший дизайн — это не только внешний вид.<br>Это то, <em>что вы чувствуете.</em>',
        introDescription:
            "Мы создаём продуманные интерьеры, объединяющие красоту, комфорт и функциональность.",
        selectedWork: "ИЗБРАННЫЕ ПРОЕКТЫ",
        portfolioTitle: "Пространства, созданные нами.",
        startProject: "Обсудить проект ↗",
        contactEyebrow: "ЕСТЬ ИДЕЯ ДЛЯ ПРОЕКТА?",
        contactTitle: "Давайте создадим<br><em>что-то особенное.</em>",
        whatsappButton: "Написать в WhatsApp",
        footerTagline: "Продуманные пространства. Вневременной стиль.",
        noProjects: "Скоро здесь появятся проекты.",
        loadError: "Не удалось загрузить проекты. Попробуйте позже.",
        projectFallback: "Дизайн интерьера"
    },
    kk: {
        navHome: "Басты бет",
        navPortfolio: "Жобалар",
        navServices: "Қызметтер",
        navContact: "Байланыс",
        contactButton: "Байланысу ↗",
        eyebrow: "ИНТЕРЬЕР ДИЗАЙНЫ СТУДИЯСЫ",
        heroTitle: 'Сізді бейнелейтін<br><em>кеңістік.</em>',
        heroDescription:
            "Өмір салтыңызға сай ойластырылған интерьерлер, табиғи материалдар және уақытқа бағынбайтын дизайн.",
        exploreWork: "Жобаларды көру",
        heroNote: "ТҰРҒЫН · КОММЕРЦИЯЛЫҚ · ЖЕКЕ ЖОБАЛАР",
        imageCaption: "01 / Жайлылық мекені",
        approach: "БІЗДІҢ ҰСТАНЫМ",
        introTitle:
            'Жақсы дизайн — тек сыртқы көрініс емес.<br>Ол сізге <em>сыйлайтын сезім.</em>',
        introDescription:
            "Біз әсемдік, жайлылық және күнделікті қолайлылықты біріктіретін интерьерлер жасаймыз.",
        selectedWork: "ТАҢДАЛҒАН ЖОБАЛАР",
        portfolioTitle: "Біз жасаған кеңістіктер.",
        startProject: "Жобаны бастау ↗",
        contactEyebrow: "ЖОБАҢЫЗ БАР МА?",
        contactTitle: "Бірге<br><em>ерекше дүние жасайық.</em>",
        whatsappButton: "WhatsApp арқылы жазу",
        footerTagline: "Ойластырылған кеңістік. Мәңгілік стиль.",
        noProjects: "Жобалар жақында қосылады.",
        loadError: "Жобаларды жүктеу мүмкін болмады. Кейінірек көріңіз.",
        projectFallback: "Интерьер дизайны"
    }
};

let currentLanguage = "ru";
let cachedProjects = [];

function applyTranslations(language) {
    currentLanguage = language;
    document.documentElement.lang = language;

    document.querySelectorAll("[data-i18n]").forEach((element) => {
        const key = element.dataset.i18n;
        const value = translations[language][key];

        if (value !== undefined) {
            element.innerHTML = value;
        }
    });

    document.querySelectorAll("[data-lang]").forEach((button) => {
        const active = button.dataset.lang === language;
        button.classList.toggle("active", active);
        button.setAttribute("aria-pressed", String(active));
    });

    loadProjects();
}

function renderProjects() {
    const projectList = document.getElementById("project-list");
    if (!projectList) return;

    projectList.replaceChildren();

    if (cachedProjects.length === 0) {
        projectList.textContent =
            translations[currentLanguage].noProjects;
        return;
    }

    cachedProjects.forEach((project) => {
        const card = document.createElement("article");
        card.className = "project-card";

        if (project.coverImage) {
            const image = document.createElement("img");
            image.src = project.coverImage;
            image.alt =
                project.displayTitle ||
                project.title ||
                translations[currentLanguage].projectFallback;
            image.loading = "lazy";

            image.addEventListener("error", () => {
                image.remove();
            }, { once: true });

            card.appendChild(image);
        }

        const title = document.createElement("h3");
        title.textContent =
            project.displayTitle ||
            project.title ||
            translations[currentLanguage].projectFallback;

        const details = document.createElement("p");
        const parts = [];

        if (project.displayStyle || project.style) {
            parts.push(project.displayStyle || project.style);
        }

        if (project.area != null) {
            parts.push(`${project.area} m²`);
        }

        details.textContent = parts.join(" · ");

        card.append(title, details);

        if (project.displayDescription || project.description) {
            const description = document.createElement("p");
            description.className = "project-description";
            description.textContent =
                project.displayDescription || project.description;
            card.appendChild(description);
        }

        const gallery = document.createElement("div");
        gallery.className = "project-gallery";
        gallery.dataset.projectId = project.id;

        const galleryMessage = document.createElement("p");
        galleryMessage.textContent = "Загрузка чертежей…";
        gallery.appendChild(galleryMessage);

        card.appendChild(gallery);

        loadProjectImages(project.id, gallery);

        projectList.appendChild(card);
    });
}


async function loadProjectImages(projectId, gallery) {
    try {
        const response = await fetch(
            `/api/projects/${projectId}/images`
        );

        if (!response.ok) {
            throw new Error("Не удалось загрузить галерею");
        }

        const images = await response.json();
        gallery.replaceChildren();

        if (images.length === 0) {
            return;
        }

        images.sort((a, b) => a.displayOrder - b.displayOrder);

        const planTitles = [
            "Обмерный план",
            "Планировочное решение",
            "План освещения",
            "План электрики"
        ];

        images.forEach((item, index) => {
            const card = document.createElement("div");
            card.className = "project-gallery-card";

            const image = document.createElement("img");
            image.src = item.imageUrl;
            image.alt = planTitles[index] || `Чертёж ${index + 1}`;
            image.loading = "lazy";

            image.addEventListener("click", () => {
                openLightbox(images, index);
            });

            image.addEventListener("error", () => {
                card.remove();
            }, { once: true });

            const caption = document.createElement("p");
            caption.className = "project-gallery-caption";
            caption.textContent =
                planTitles[index] || `Чертёж ${index + 1}`;

            card.appendChild(image);
            card.appendChild(caption);
            gallery.appendChild(card);
        });
    } catch (error) {
        console.error("Ошибка загрузки галереи:", error);
        gallery.replaceChildren();
    }
}



async function loadProjectTranslation(project, language) {
    // Русский — основной язык, поэтому отдельный перевод не нужен.
    if (language === "ru") {
        return {
            ...project,
            displayTitle: project.title,
            displayDescription: project.description,
            displayStyle: project.style
        };
    }

    try {
        const response = await fetch(
            `/api/projects/${project.id}/translations/${language}`
        );

        if (!response.ok) {
            // Перевода нет — используем основной текст проекта.
            return {
                ...project,
                displayTitle: project.title,
                displayDescription: project.description,
                displayStyle: project.style
            };
        }

        const translation = await response.json();

        return {
            ...project,
            displayTitle: translation.title || project.title,
            displayDescription: translation.description || project.description,
            displayStyle: translation.style || project.style
        };
    } catch (error) {
        console.error(
            `Could not load ${language} translation for project ${project.id}:`,
            error
        );

        return {
            ...project,
            displayTitle: project.title,
            displayDescription: project.description,
            displayStyle: project.style
        };
    }
}



async function loadProjects() {
    const projectList = document.getElementById("project-list");

    try {
        const response = await fetch("/api/projects");

        if (!response.ok) {
            throw new Error("Failed to load projects");
        }

        const projects = await response.json();
        const baseProjects = Array.isArray(projects) ? projects : [];

        cachedProjects = await Promise.all(
            baseProjects.map((project) =>
                loadProjectTranslation(project, currentLanguage)
            )
        );

        renderProjects();
    } catch (error) {
        console.error("Error loading projects:", error);
        projectList.textContent = translations[currentLanguage].loadError;
    }
}


document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("[data-lang]").forEach((button) => {
        button.addEventListener("click", () => {
            applyTranslations(button.dataset.lang);
        });
    });

    document.querySelectorAll(".navigation a").forEach((link, index) => {
        const keys = ["navHome", "navPortfolio", "navServices", "navContact"];
        link.dataset.i18n = keys[index];
    });

    document.querySelector(".hero .eyebrow").dataset.i18n = "eyebrow";
    document.querySelector(".hero-description").dataset.i18n = "heroDescription";
    document.querySelector(".hero .primary-button").dataset.i18n = "exploreWork";
    document.querySelector(".hero-note").dataset.i18n = "heroNote";
    document.querySelector(".image-caption").dataset.i18n = "imageCaption";

    document.querySelector(".intro .eyebrow").dataset.i18n = "approach";
    document.querySelector(".intro h2").dataset.i18n = "introTitle";
    document.querySelector(".intro > p:last-child").dataset.i18n = "introDescription";

    document.querySelector(".section-heading .eyebrow").dataset.i18n = "selectedWork";
    document.querySelector(".section-heading h2").dataset.i18n = "portfolioTitle";
    document.querySelector(".text-link").dataset.i18n = "startProject";

    document.querySelector(".contact .eyebrow").dataset.i18n = "contactEyebrow";
    document.querySelector(".contact h2").dataset.i18n = "contactTitle";
    document.querySelector(".contact .primary-button").dataset.i18n = "whatsappButton";
    document.querySelector(".footer > p:nth-of-type(1)").dataset.i18n = "footerTagline";
    document.querySelector(".header-contact").dataset.i18n = "contactButton";

    applyTranslations("ru");
    loadProjects();
});

let currentLightboxImages = [];
let currentLightboxIndex = 0;

function openLightbox(images, index) {
    currentLightboxImages = images;
    currentLightboxIndex = index;

    let lightbox = document.getElementById("project-lightbox");

    if (!lightbox) {
        lightbox = document.createElement("div");
        lightbox.id = "project-lightbox";
        lightbox.className = "project-lightbox";

        lightbox.innerHTML = `
<button class="lightbox-close" aria-label="Закрыть">&times;</button>
<button class="lightbox-prev" aria-label="Предыдущий чертёж">&#10094;</button>
<img class="lightbox-image" alt="Чертёж проекта">
    <button class="lightbox-next" aria-label="Следующий чертёж">&#10095;</button>
    <p class="lightbox-counter"></p>
    `;

    document.body.appendChild(lightbox);

    lightbox.querySelector(".lightbox-close").addEventListener("click", closeLightbox);

    lightbox.querySelector(".lightbox-prev").addEventListener("click", () => {
    showLightboxImage(currentLightboxIndex - 1);
});

    lightbox.querySelector(".lightbox-next").addEventListener("click", () => {
    showLightboxImage(currentLightboxIndex + 1);
});

    lightbox.addEventListener("click", (event) => {
    if (event.target === lightbox) {
    closeLightbox();
}
});

    document.addEventListener("keydown", handleLightboxKeydown);
    }

    lightbox.classList.add("active");
    document.body.style.overflow = "hidden";

    showLightboxImage(currentLightboxIndex);
    }

    function showLightboxImage(index) {
    if (currentLightboxImages.length === 0) return;

    const total = currentLightboxImages.length;

    currentLightboxIndex = (index + total) % total;

    const item = currentLightboxImages[currentLightboxIndex];
    const lightbox = document.getElementById("project-lightbox");

    lightbox.querySelector(".lightbox-image").src = item.imageUrl;
    lightbox.querySelector(".lightbox-image").alt =
    `Чертёж ${currentLightboxIndex + 1}`;

    lightbox.querySelector(".lightbox-counter").textContent =
    `${currentLightboxIndex + 1} / ${total}`;
}

    function closeLightbox() {
    const lightbox = document.getElementById("project-lightbox");

    if (lightbox) {
    lightbox.classList.remove("active");
}

    document.body.style.overflow = "";
}

    function handleLightboxKeydown(event) {
    const lightbox = document.getElementById("project-lightbox");

    if (!lightbox || !lightbox.classList.contains("active")) return;

    if (event.key === "Escape") {
    closeLightbox();
} else if (event.key === "ArrowRight") {
    showLightboxImage(currentLightboxIndex + 1);
} else if (event.key === "ArrowLeft") {
    showLightboxImage(currentLightboxIndex - 1);
}
}

