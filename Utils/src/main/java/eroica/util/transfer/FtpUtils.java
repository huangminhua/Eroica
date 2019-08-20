package eroica.util.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * FTP tools.
 * 
 * @author Minhua HUANG
 *
 */
public class FtpUtils {
	private static FTPClient connect(String remoteHost, int remotePort, String ftpUsername, String ftpPassword)
			throws SocketException, IOException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(remoteHost, remotePort);
		ftpClient.login(ftpUsername, ftpPassword);
		ftpClient.setControlEncoding(System.getProperty("file.encoding"));
		int reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply))
			throw new RuntimeException("Connecting to ftp://" + remoteHost + ":" + remotePort + " failed.");
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		return ftpClient;
	}

	/**
	 * Upload a file or directory recursively.
	 * 
	 * @param localFile   local file
	 * @param remoteHost  ftp server host
	 * @param remotePort  ftp server port
	 * @param remoteDir   remote directory where to upload local file to
	 * @param ftpUsername ftp username
	 * @param ftpPassword ftp password
	 * @throws IOException     thrown by ftp processes
	 * @throws SocketException thrown by ftp processes
	 */
	public static void putRecursively(File localFile, String remoteHost, int remotePort, String remoteDir,
			String ftpUsername, String ftpPassword) throws SocketException, IOException {
		FTPClient ftpClient = null;
		try {
			ftpClient = connect(remoteHost, remotePort, ftpUsername, ftpPassword);
			putRecursively(ftpClient, localFile, remoteDir);
			ftpClient.logout();
		} finally {
			if (ftpClient != null && ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
	}

	private static void putRecursively(FTPClient ftpClient, File localFile, String remoteDir) throws IOException {
		if (localFile.isDirectory()) // 上传目录
			putDirectoryRecursively(ftpClient, localFile, remoteDir);
		else if (localFile.isFile()) // 上传单个文件
			putFile(ftpClient, localFile, remoteDir);
		else
			throw new RuntimeException("Type of file " + localFile.getAbsolutePath() + " is unknown.");
	}

	private static void putFile(FTPClient ftpClient, File localFile, String remoteDir) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(localFile);
			if (!ftpClient.storeFile(remoteDir + "/" + localFile.getName(), is))
				throw new RuntimeException(
						"Failed to upload file " + localFile.getAbsolutePath() + " to " + remoteDir + ".");
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
		}
	}

	private static void putDirectoryRecursively(FTPClient ftpClient, File localDir, String remoteDir)
			throws IOException {
		String remoteSubDir = remoteDir + "/" + localDir.getName();
		ftpClient.makeDirectory(remoteSubDir);
		File[] subFiles = localDir.listFiles();
		for (int i = 0; i < subFiles.length; i++)
			putRecursively(ftpClient, subFiles[i], remoteSubDir);
	}
}
