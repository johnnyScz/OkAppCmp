-optimizationpasses 5

-dontusemixedcaseclassnames

-dontskipnonpubliclibraryclasses
-dontoptimize
-dontpreverify

-keepattributes *Annotation*

-dontwarn com.squareup.okhttp.**
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn rx.**
-dontwarn retrofit.**
-keepattributes Signature

-keep public class * extends android.support.v4.app.Fragment

-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn android.support.**

-keep class com.google.gson.** {*;}

-keep class retrofit2.** { *; }

-keep class retrofit.** { *; }

-keep class rxjava.** { *; }

-keep class rxjava2.** { *; }

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}


-keepattributes *Annotation*

-keepattributes *BindView*


-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class * implements java.io.Serializable { *; }

-keep public class * extends android.support.v4.app.Fragment   
-keep public class * extends android.app.Activity 
-keep public class * extends android.app.Application
-keepattributes *Annotation*,InnerClasses   
 -keep public class * extends android.app.Service   
 -keep public class * extends android.content.BroadcastReceiver   
 -keep public class * extends android.preference.Preference

 -keep class !com.xinyu.newdiggtest.** {*;}
 -dontwarn **

 -keepclasseswithmembernames class * {
        native ;
  }

  -keepclassmembers class * extends android.app.Activity {

       public void *(android.view.View);

  }

  -keepclassmembers class * {
      @org.greenrobot.eventbus.Subscribe <methods>;
  }
  #WebView
  -keepclassmembers class * extends android.webkit.WebView {*;}
  -keepclassmembers class * extends android.webkit.WebViewClient {*;}
  -keepclassmembers class * extends android.webkit.WebChromeClient {*;}
  -keepclassmembers class * {
      @android.webkit.JavascriptInterface <methods>;
  }
 -keepclassmembers class fqcn.of.javascript.interface.for.webview {

       public *;

    }

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}






