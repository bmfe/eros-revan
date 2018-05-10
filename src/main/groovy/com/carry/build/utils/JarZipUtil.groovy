package com.carry.build.utils

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class JarZipUtil {
    public static List unzipJar(String jarPath, String destDirPath) {

        List list = new ArrayList()
        if (jarPath.endsWith('.jar')) {

            JarFile jarFile = new JarFile(jarPath)
            Enumeration<JarEntry> jarEntrys = jarFile.entries()
            while (jarEntrys.hasMoreElements()) {
                JarEntry jarEntry = jarEntrys.nextElement()
                if (jarEntry.directory) {
                    continue
                }
                String entryName = jarEntry.getName()
                if (entryName.endsWith('.class')) {
                    String className = entryName.replace(File.separator, '.')
                    list.add(className)
                    String outFileName = destDirPath + File.separator + entryName
                    File outFile = new File(outFileName)
                    outFile.getParentFile().mkdirs()
                    InputStream inputStream = jarFile.getInputStream(jarEntry)
                    FileOutputStream fileOutputStream = new FileOutputStream(outFile)
                    fileOutputStream << inputStream
                    fileOutputStream.close()
                    inputStream.close()
                }

            }
            jarFile.close()
        }
        return list

    }


    static boolean findClass(String jarPath, String className) {
        if (jarPath.endsWith(".jar")) {
            JarFile jarFile = new JarFile(jarPath)
            Enumeration<JarEntry> jarEntrys = jarFile.entries()
            while (jarEntrys.hasMoreElements()) {
                JarEntry jarEntry = jarEntrys.nextElement()
                if (jarEntry.directory) {
                    continue
                }
                String entryName = jarEntry.getName()
                if (entryName.endsWith('.class')) {
                    String name = entryName.replace(File.separator,'.')
                    if (name.contains(className)) {
                        return true;
                    }
                    continue

                }
            }
            jarFile.close()
        }
        return false;
    }

    public static void zipJar(String packagePath, String destPath) {

        File file = new File(packagePath)
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(destPath))
        file.eachFileRecurse { File f ->
            String entryName = f.getAbsolutePath().substring(packagePath.length() + 1)
            if (!f.directory) {
                ZipEntry zipEntry = new ZipEntry(entryName)
                outputStream.putNextEntry(zipEntry)
                InputStream inputStream = new FileInputStream(f)
                outputStream << inputStream
                inputStream.close()
                outputStream.closeEntry()
            }
        }
        outputStream.close()
    }
}
