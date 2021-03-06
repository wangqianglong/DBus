/*-
 * <<
 * DBus
 * ==
 * Copyright (C) 2016 - 2018 Bridata
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package com.creditease.dbus.heartbeat.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.commons.lang.StringUtils;

import com.creditease.dbus.heartbeat.log.LoggerFactory;

public class IOUtil {

    public static boolean isFile(File file) {
        boolean isOk = false;
        try {
            isOk = file.isFile();
        } catch (Exception e) {
            LoggerFactory.getLogger().error("[isFile-error]", e);
            isOk = false;
        }
        return isOk;
    }

    public static int exists(File file) {
        int status = -1;
        try {
            status = file.exists() ? 1 : 0;
        } catch (Exception e) {
            LoggerFactory.getLogger().error("[exists-error]", e);
            status = -1;
        }
        return status;
    }

    public static boolean delete(File file) {
        boolean isOk = false;
        try {
            isOk = file.delete();
        } catch (Exception e) {
            LoggerFactory.getLogger().error("[delete-error]", e);
            isOk = false;
        }
        return isOk;
    }

    public static File[] listFiles(File file) {
        File[] files = null;
        try {
            File dir = file.getParentFile();
            final String fileName = StringUtils.left(file.getName(), StringUtils.lastIndexOf(file.getName(), "."));
            files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith(fileName);
                }
            });
        } catch (Exception e) {
            LoggerFactory.getLogger().error("[listFiles-error]", e);
            files = null;
        }
        return files;
    }

    public static void close(Closeable obj) {
        if (obj != null)
            try {
                obj.close();
            } catch (IOException e) {
                LoggerFactory.getLogger().error("[stream-close-error]", e);
            }
    }

    public static void close(FileChannel obj) {
        if (obj != null)
            try {
                obj.close();
            } catch (IOException e) {
                LoggerFactory.getLogger().error("[channel-close-error]", e);
            }
    }

    public static boolean createNewFile(File target) {
        boolean isOk = false;
        try {
            target.getParentFile().mkdirs();
            isOk = target.createNewFile();
        } catch (IOException e) {
            LoggerFactory.getLogger().error("[file-create-error]", e);
            isOk = false;
        }
        return isOk;
    }

    public static boolean copy(File src, File target) {
        boolean isOk = true;
        FileInputStream fis = null;
        FileChannel srcFc = null;
        FileOutputStream fos = null;
        FileChannel targetFc = null;
        try {
            fis = new FileInputStream(src);
            srcFc = fis.getChannel();
            fos = new FileOutputStream(target);
            targetFc = fos.getChannel();
            targetFc.truncate(0);
            srcFc.transferTo(0, src.length(), targetFc);
        } catch (Exception e) {
            LoggerFactory.getLogger().error("[file-copy-error]", e);
            isOk = false;
        } finally {
            close(srcFc);
            close(fis);
            close(targetFc);
            close(fos);
        }
        return isOk;
    }

}
