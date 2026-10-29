# Pop TV

مشغّل Android TV مصمم لجهاز Freebox Pop. يوفر شبكة تطبيقات من ستة أعمدة، ودوّارة لعناوين TMDB، وملفات تعريف محلية، وإعدادات وأدوات تحكم بالنظام.

## المتطلبات

- JDK 21
- Android SDK مع منصة API 36
- Gradle Wrapper المرفق بالمستودع

## الإعداد

انسخ `local.properties.example` إلى `local.properties`، واضبط `sdk.dir` وأضف رمز TMDB Read Access إلى `TMDB_READ_TOKEN`. لا تضع الرمز في المستودع.

## البناء

```sh
./gradlew :app:assembleRelease
```

يوجد APK الناتج في `app/build/outputs/apk/release/`.

## البنية

- `features/home`: الشاشة الرئيسية والتنقل والشبكة
- `features/search` و`features/settings`: البحث والإعدادات
- `features/control`: مركز التحكم والملفات الشخصية وأزرار التحكم
- `features/screensaver`: شاشات Aerial ومواقيت الصلاة
- `data/tmdb`: الاتجاهات وذاكرة دوّارة العناوين
- `core`: التصميم والتفضيلات والمنطق المشترك
- `platform/recovery`: العودة إلى مشغّل Free عند حدوث مشكلة

يتضمن المشروع مكونات واعتماديات من مشاريع أخرى؛ يجب الحفاظ على إشعارات التراخيص والإسناد الخاصة بها.
