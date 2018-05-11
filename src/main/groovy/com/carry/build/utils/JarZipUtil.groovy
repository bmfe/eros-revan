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
                    String className = entryName.replace("/", '.').replace("\\", '.')
                    list.add(className)
                    String outFileName = destDirPath + File.separator + entryName.replace('/', File.separator)
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
                    String name = entryName.replace("/", '.').replace("\\", ".")
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

        File base = new File(packagePath)
        File source = new File(packagePath + File.separator + "com")
        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(destPath))
        zip(base, source, outputStream)
        outputStream.close()
//        file.eachFileRecurse { File f ->
//            String entryName = f.getAbsolutePath().substring(packagePath.length() + 1)
//            if (!f.directory) {
//                ZipEntry zipEntry = new ZipEntry(entryName)
//                outputStream.putNextEntry(zipEntry)
//                InputStream inputStream = new FileInputStream(f)
//                outputStream << inputStream
//                inputStream.close()
//                outputStream.closeEntry()
//            }
//        }

    }


    static void zip(File base, File source, JarOutputStream target) {
        BufferedInputStream inputStream = null;
        try {
            if (source.isDirectory()) {
                String name = source.getPath().replace("\\", "/");
                name = name.substring(base.getAbsolutePath().length() + 1)
                if (!name.isEmpty()) {
                    if (!name.endsWith("/"))
                        name += "/";
                    JarEntry entry = new JarEntry(name);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                for (File nestedFile : source.listFiles())
                    zip(base, nestedFile, target);
                return;
            }
            if (!source.getAbsolutePath().endsWith(".class")) return
            String fileName = source.getPath().replace("\\", "/");
            fileName = fileName.substring(base.getAbsolutePath().length() + 1)

            JarEntry entry = new JarEntry(fileName);
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            inputStream = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while (true) {
                int count = inputStream.read(buffer);
                if (count == -1)
                    break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        }
        finally {
            if (inputStream != null)
                inputStream.close();
        }
    }
}
