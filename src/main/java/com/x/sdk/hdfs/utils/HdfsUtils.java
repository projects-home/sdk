package com.x.sdk.hdfs.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.x.sdk.constant.ExceptionCodeConstant;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.x.base.exception.SystemException;
import com.x.sdk.ccs.util.ConfigTool;

/**
 * HDFS 工具类 Date: 2017年5月17日 <br>
 * Copyright (c) 2017 x.com <br>
 * 
 * @author panyl
 */
public class HdfsUtils {
    public static final Logger LOG = LoggerFactory.getLogger(HdfsUtils.class);

    private static String defaultFS = null;
    static {
        defaultFS = ConfigTool.getHdfsConfigByRest().getDefaultFS();
    }

    /**
     * 获取文件系统
     * 
     * @return
     * @author panyl
     */
    public static FileSystem getFileSystem() {
        FileSystem fs = null;
        try {
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", defaultFS);
            fs = FileSystem.get(conf);
            System.out.println(fs);
        } catch (Exception e) {
            LOG.error("hdfs 连接出现异常", e);
            throw new SystemException(e);
        }
        return fs;
    }

    /**
     * 查看当前路径是否存在
     * 
     * @param fs
     * @param path
     * @return
     */
    public static Boolean checkPathExist(FileSystem fs, String path) {
        Boolean isExist = true;
        try {
            isExist = fs.exists(new Path(path));
        } catch (IOException e) {
            isExist = false;
            LOG.error("检查文件是否存在异常path=" + path, e);
        }
        return isExist;
    }

    public static boolean copyLocalFile2HDFS(String localFile, String hdfsFile) {
        if (StringUtils.isEmpty(localFile) || StringUtils.isEmpty(hdfsFile)) {
            return false;
        }
        FileSystem hdfs = getFileSystem();
        Path src = new Path(localFile);
        Path dst = new Path(hdfsFile);
        try {
            if (!new File(localFile).exists()) {
                throw new SystemException(ExceptionCodeConstant.SYSTEM_ERROR, "源文件不存在");
            }

            if (!hdfs.exists(dst)) {
                hdfs.mkdirs(dst);
            }
            hdfs.copyFromLocalFile(src, dst);
        } catch (IOException e) {
            LOG.error("文件从本地拷贝到hdfs上，出现Io异常，导致拷贝文件失败,src:{},dst:{}", src, dst, e);
            return false;
        } finally {
            try {
                hdfs.close();
            } catch (IOException e) {
                LOG.error("error close file", e);
            }
        }
        return true;
    }

    public static Boolean copyHDFSToLocalFile(String src, String dst) {

        if (StringUtils.isEmpty(src) || StringUtils.isEmpty(dst)) {
            return false;
        }
        FileSystem hdfs = getFileSystem();
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        try {
            if (!hdfs.exists(srcPath)) {
                throw new SystemException(ExceptionCodeConstant.SYSTEM_ERROR, "源文件不存在");
            }

            if (!hdfs.exists(dstPath)) {
                hdfs.mkdirs(dstPath);
            }
            hdfs.copyToLocalFile(false, srcPath, dstPath, true);
        } catch (IOException e) {
            LOG.error("文件从hdfs拷贝到本地，出现Io异常，导致拷贝文件失败", e);
            return false;
        } finally {
            try {
                hdfs.close();
            } catch (IOException e) {
                LOG.error("error close file", e);
            }
        }
        return true;
    }

    /**
     * 获取目录path下所有的文件名
     * 
     * @param fs
     * @param path
     * @return
     */
    public static List<String> scanDir(FileSystem fs, Path path) {
        List<String> list = new ArrayList<>();
        try {
            RemoteIterator<FileStatus> remoteIterator = fs.listStatusIterator(path);
            while (remoteIterator.hasNext()) {
                list.add(remoteIterator.next().getPath().getName());
            }
        } catch (IOException e) {
            LOG.error("扫描文件异常", e);
        }
        return list;
    }

    /**
     * 递归遍历找到所有目录和文件存储在map中，文件，key：路径，value：FILE ；目录，key：路径，value：DIR
     * 
     * @param fs
     * @param src
     */
    public static void recureScanDir(FileSystem fs, Path src, Map<Path, String> map) {
        try {
            if (fs.isFile(src)) {
                map.put(src, "FILE");
            } else {
                map.remove(src);
                RemoteIterator<FileStatus> remoteIterator = fs.listStatusIterator(src);
                if (!remoteIterator.hasNext()) {
                    map.put(src, "DIR");
                } else {
                    while (remoteIterator.hasNext()) {
                        recureScanDir(fs, remoteIterator.next().getPath(), map);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("递归扫描文件异常", e);
        }
    }

    /**
     * 
     * @param newFile
     *            new file path, a full path name, may like '/tmp/test.txt'
     * @param content
     *            file content
     * @return boolean true-success, false-failed
     * @throws IOException
     *             file io exception
     */
    public static boolean createNewHDFSFile(String newFile, String content) {
        if (StringUtils.isEmpty(newFile) || null == content) {
            return false;
        }
        FileSystem hdfs = getFileSystem();
        FSDataOutputStream os = null;
        try {
            os = hdfs.create(new Path(newFile));
            os.write(content.getBytes("UTF-8"));
        } catch (IOException e) {
            LOG.error("创建文件失败", e);
            return false;
        } finally {
            try {
                os.close();
                hdfs.close();
            } catch (IOException e) {
                LOG.error("error close file", e);
            }
        }

        return true;
    }

    /**
     * 
     * @param hdfsFile
     *            a full path name, may like '/tmp/test.txt'
     * @return boolean true-success, false-failed
     * @throws IOException
     *             file io exception
     */
    public static boolean deleteHDFSFile(String hdfsFile) {
        if (StringUtils.isEmpty(hdfsFile)) {
            return false;
        }
        FileSystem hdfs = getFileSystem();
        try {
            Path path = new Path(hdfsFile);
            boolean isDeleted = hdfs.delete(path, true);
            return isDeleted;
        } catch (IOException e) {
            LOG.error("删除文件失败", e);
            return false;
        } finally {
            try {
                hdfs.close();
            } catch (IOException e) {
                LOG.error("error close file", e);
            }
        }

    }

    /**
     * 
     * @param hdfsFile
     *            a full path name, may like '/tmp/test.txt'
     * @return byte[] file content
     * @throws IOException
     *             file io exception
     */
    public static byte[] readHDFSFile(String hdfsFile) {
        if (StringUtils.isEmpty(hdfsFile)) {
            return null;
        }
        FileSystem fs = getFileSystem();
        // check if the file exists
        Path path = new Path(hdfsFile);
        try {
            if (fs.exists(path)) {
                FSDataInputStream is = fs.open(path);
                // get the file info to create the buffer
                FileStatus stat = fs.getFileStatus(path);
                // create the buffer
                byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
                is.readFully(0, buffer);
                is.close();
                fs.close();
                return buffer;
            } else {
                throw new SystemException("文件不存在");
            }
        } catch (IOException e) {
            LOG.error("读取内容", e);
            return null;
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                LOG.error("error close file", e);
            }
        }

    }

    /**
     * 
     * @param hdfsFile
     *            a full path name, may like '/tmp/test.txt'
     * @param content
     *            string
     * @return boolean
     * @throws Exception
     *             something wrong
     */
    public static boolean append(String hdfsFile, String content) {
        if (StringUtils.isEmpty(hdfsFile)) {
            return false;
        }
        if (StringUtils.isEmpty(content)) {
            return true;
        }
        Configuration conf = new Configuration();
        // solve the problem when appending at single datanode hadoop env
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        FileSystem fs = null;
        Path path = new Path(hdfsFile);
        try {
            fs = FileSystem.get(URI.create(hdfsFile), conf);
            if (fs.exists(path)) {
                try {
                    InputStream in = new ByteArrayInputStream(content.getBytes());
                    OutputStream out = fs.append(new Path(hdfsFile));
                    IOUtils.copyBytes(in, out, 4096, true);
                    out.close();
                    in.close();
                    fs.close();
                } catch (Exception ex) {
                    fs.close();
                    throw ex;
                }
            } else {
                HdfsUtils.createNewHDFSFile(hdfsFile, content);
            }

        } catch (IOException e) {
            LOG.error("添加内容", e);
            return false;
        } finally {
            try {
                fs.close();
            } catch (IOException e) {
                LOG.error("error close file", e);
            }
        }
        return true;
    }

}
