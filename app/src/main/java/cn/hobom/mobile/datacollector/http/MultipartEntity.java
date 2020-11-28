package cn.hobom.mobile.datacollector.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 参�? http://blog.rafaelsanches.com/2011/01/29/upload-using-multipart-post-
 * using-httpclient-in-android/)
 * 
 */
public class MultipartEntity implements HttpEntity {
	private String boundary = "---7d4a6d158c9";
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	boolean isSetLast = false;
	boolean isSetFirst = false;
	public MultipartEntity() {

	}

	public void writeFirstBoundaryIfNeeds() {
		if (!isSetFirst) {
			try {
				out.write(("--" + boundary + "\r\n").getBytes());
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		isSetFirst = true;
	}

	public void writeLastBoundaryIfNeeds() {
		if (isSetLast) {
			return;
		}
		try {
			out.write(("\r\n--" + boundary + "--\r\n").getBytes());
		} catch (final IOException e) {
			e.printStackTrace();
		}
		isSetLast = true;
	}

	public void addPart(final String key, final String value) {
		writeFirstBoundaryIfNeeds();
		try {
			out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
			out.write(value.getBytes());
			out.write(("\r\n--" + boundary + "\r\n").getBytes());
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void addPart(final String key, final String fileName, final byte[] data, final boolean isLast) {
		addPart(key, fileName, data, "application/octet-stream", isLast);
	}

	public void addPart(final String key, final String fileName, final InputStream fin, final boolean isLast) {
		addPart(key, fileName, fin, "application/octet-stream", isLast);
	}

	public void addPart(final String key, final String fileName, final InputStream fin, String type, final boolean isLast) {
		writeFirstBoundaryIfNeeds();
		try {
			type = "Content-Type: " + type + "\r\n";
			out.write(("Content-Disposition: form-data; name=\"" + key
					+ "\"; filename=\"" + fileName + "\"\r\n").getBytes());
			out.write(type.getBytes());
			out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

			final byte[] tmp = new byte[4096];
			int l = 0;
			while ((l = fin.read(tmp)) != -1) {
				out.write(tmp, 0, l);
			}
			if (!isLast)
				out.write(("\r\n--" + boundary + "\r\n").getBytes());
			out.flush();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fin.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void addPart(final String key, final File value, final boolean isLast) {
		try {
			addPart(key, value.getName(), new FileInputStream(value), isLast);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addPart(final String key, final String fileName,
			final byte[] data, String type, final boolean isLast) {
		writeFirstBoundaryIfNeeds();
		try {
			type = "Content-Type: " + type + "\r\n";
			out.write(("Content-Disposition: form-data; name=\"" + key
					+ "\"; filename=\"" + fileName + "\"\r\n").getBytes());
			out.write(type.getBytes());
			out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

			out.write(data);
			if (!isLast)
				out.write(("\r\n--" + boundary + "\r\n").getBytes());
			out.flush();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getContentLength() {
		writeLastBoundaryIfNeeds();
		return out.toByteArray().length;
	}

	@Override
	public Header getContentType() {
		return new BasicHeader("Content-Type", "multipart/form-data; boundary="
				+ boundary);
	}

	@Override
	public boolean isChunked() {
		return false;
	}

	@Override
	public boolean isRepeatable() {
		return false;
	}

	@Override
	public boolean isStreaming() {
		return false;
	}

	@Override
	public void writeTo(final OutputStream outstream) throws IOException {
		outstream.write(out.toByteArray());
	}

	@Override
	public Header getContentEncoding() {
		return null;
	}

	@Override
	public void consumeContent() throws IOException,
			UnsupportedOperationException {
		if (isStreaming()) {
			throw new UnsupportedOperationException(
					"Streaming entity does not implement #consumeContent()");
		}
	}

	@Override
	public InputStream getContent() throws IOException,
			UnsupportedOperationException {
		return new ByteArrayInputStream(out.toByteArray());
	}
}