# نظرة عامة على مشروع Pop TV

Pop TV هو مشغّل Kotlin لنظام Android TV على جهاز Freebox Pop. يتضمن شبكة رئيسية من ستة أعمدة، ودوّارة TMDB مخزنة مؤقتًا، وملفات تعريف محلية، وإعدادات ومسار استعادة إلى Free TV.

## البناء

ثبّت JDK 21 وAndroid SDK API 36. أعد إعداد `local.properties` بالاستناد إلى `local.properties.example`، ثم شغّل `./gradlew :app:assembleRelease`.

## المكونات الرئيسية

راجع `README.md` لبنية المصدر وتعليمات البناء، و`AGENTS.md` لمتطلبات المساهمة. تستلهم الواجهة بعض أنماط التفاعل في tvOS، وتُستخدم إرشادات Apple مرجعًا للتصميم.
