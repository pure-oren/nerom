# Retrofit rules
-keep class retrofit2.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-keep interface retrofit2.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**

# Gson rules
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-dontwarn com.google.gson.**

# Glide rules
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Model classes
-keep class com.movieapp.explorer.data.model.** { *; }

# Room database
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keepclassmembers class * {
    @androidx.room.PrimaryKey <fields>;
    @androidx.room.ColumnInfo <fields>;
    @androidx.room.Embedded <fields>;
    @androidx.room.Relation <fields>;
}

# ViewModel
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Keep androidx classes
-keep class androidx.** { *; }
-dontwarn androidx.**

# Application classes
-keep class com.movieapp.explorer.** { *; }
-keepclassmembers class com.movieapp.explorer.** {
    public <methods>;
}
