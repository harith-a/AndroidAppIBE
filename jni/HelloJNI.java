public class HelloJNI {
   static {
   // Load native library at runtime
   // hello.dll (Windows) or libhello.so (Unixes)
   // libhello.jnilib (OSX)
      System.loadLibrary("ibe");
   }
 
   // Test Driver
   public static void main(String[] args) {
      new ibeJNI().myencrypt();
   }
}
