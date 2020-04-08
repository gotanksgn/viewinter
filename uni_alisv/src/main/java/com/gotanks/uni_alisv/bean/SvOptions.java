package com.gotanks.uni_alisv.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MT on 2020/4/8.
 */
public class SvOptions implements Parcelable {

    /**
     * fileName : 1586314625129.mp4
     * imageType : cover
     * imageUploadAuth : {"imageId":"90e54fcf199e4fefb6da3259615c22c5","imageURL":"https://outin-ff75076a297a11eaa04d00163e1c9256.oss-cn-shanghai.aliyuncs.com/image/cover/4A6B027F34B44F2FB8C42DEF5C82FCAB-6-2.png?Expires=1586318226&OSSAccessKeyId=LTAIxSaOfEzCnBOj&Signature=w6XVvUbKni6aLo5Pn%2F9g9fQF0b4%3D","uploadAddress":"eyJFbmRwb2ludCI6Imh0dHBzOi8vb3NzLWNuLXNoYW5naGFpLmFsaXl1bmNzLmNvbSIsIkJ1Y2tldCI6Im91dGluLWZmNzUwNzZhMjk3YTExZWFhMDRkMDAxNjNlMWM5MjU2IiwiRmlsZU5hbWUiOiJpbWFnZS9jb3Zlci80QTZCMDI3RjM0QjQ0RjJGQjhDNDJERUY1QzgyRkNBQi02LTIucG5nIn0=","uploadAuth":"eyJTZWN1cml0eVRva2VuIjoiQ0FJUzB3UjFxNkZ0NUIyeWZTaklyNWJHUDR6YWkrbDcxZnJjUkhMVW5sbG5Qclo1bjZxZDBEejJJSGhKZVhOdkJPMGV0ZjQrbVdCWTdQY1lsclVxRU1BYkhSR2RONUV0dHNvS3JGejZKcGZadjh1ODRZQURpNUNqUWRNaTBNa0dtcDI4V2Y3d2FmK0FVQkxHQ1RtZDVNQVlvOWJUY1RHbFFDWnVXLy90b0pWN2I5TVJjeENsWkQ1ZGZybC9MUmRqcjhsbzF4R3pVUEcyS1V6U24zYjNCa2hsc1JZZTcyUms4dmFIeGRhQXpSRGNnVmJtcUpjU3ZKK2pDNEM4WXM5Z0c1MTlYdHlwdm9weGJiR1Q4Q05aNXo5QTlxcDlrTTQ5L2l6YzdQNlFIMzViNFJpTkw4L1o3dFFOWHdoaWZmb2JIYTlZcmZIZ21OaGx2dkRTajQzdDF5dFZPZVpjWDBha1E1dTdrdTdaSFArb0x0OGphWXZqUDNQRTNyTHBNWUx1NFQ0OFpYVVNPRHREWWNaRFVIaHJFazRSVWpYZEk2T2Y4VXJXU1FDN1dzcjIxN290ZzdGeXlrM3M4TWFIQWtXTFg3U0IyRHdFQjRjNGFFb2tWVzRSeG5lelc2VUJhUkJwYmxkN0JxNmNWNWxPZEJSWm9LK0t6UXJKVFg5RXoycExtdUQ2ZS9MT3M3b0RWSjM3V1p0S3l1aDRZNDlkNFU4clZFalBRcWl5a1Qwa0ZncGZUSzFSemJQbU5MS205YmFCMjUvelcrUGREZTBkc1Znb0pWS0RwaUdXRzNSTE5uK3p0Sjl4YmtlRStzS1V3cUtUcnNzL1RnSjB0NDlXVkZpSWVOODE4MUErdS9Mc3RCbksrNzY1RHk3dC8yOHg5ZFNmdmFzM3NCYzdKS2o4M3JYTjVHZU81Q3pCUDVOVXdwbUhCRGRkSmoyc1lHRjh6ZnlvZ1hZS21nc01pV25jT1d4RXN3bk1qem50SVpWQWlLM1dsaU1lVS8xSjVjM2NTaWE5K0Z0bkJlbUE2cTB3UmZoWWUrUkRRajQxV0wydlExdU5Hb0FCVTdmWlNWbVVBTzhjZ2RiLzc2cXJib04zZUJIVnpYcHpnbmxqRmtBTXErZmxzVjl2d2Z2VG1hSzJoOTJYbVJZL0F1TE80QXdCQmFhTnpZbTBpM29GNEN1UTcyQVBldjFiVDlHM3pmcjd4Uk9MN0xMai9PTzN4NHlHS0N1VmZvOVFKcWJKcXgydVhNeitoQm1nL3hWSGhFT0MyQWRGb2kvNEFDZmpXM1FiSFFFPSIsIkFjY2Vzc0tleUlkIjoiU1RTLk5Vc3Q2bmY2WmI4N0ZUZXpZMjI5VXBvNjIiLCJFeHBpcmVVVENUaW1lIjoiMjAyMC0wNC0wOFQwMzo1NDozN1oiLCJBY2Nlc3NLZXlTZWNyZXQiOiJEUEFqYmVZMzhwcXM4WWFjOTd3NUxhMzR0R3l5S25GWkxlU0x1NGY4M2tQQiIsIkV4cGlyYXRpb24iOiIzNDUxIiwiUmVnaW9uIjoiY24tc2hhbmdoYWkifQ=="}
     * question : 测试问题。。。。
     * title : 1586314625129
     * videoUploadAuth : {"uploadAddress":"eyJFbmRwb2ludCI6Imh0dHBzOi8vb3NzLWNuLXNoYW5naGFpLmFsaXl1bmNzLmNvbSIsIkJ1Y2tldCI6Im91dGluLWZmNzUwNzZhMjk3YTExZWFhMDRkMDAxNjNlMWM5MjU2IiwiRmlsZU5hbWUiOiJzdi80ZTU4NDdiMi0xNzE1N2I4NDYwNC80ZTU4NDdiMi0xNzE1N2I4NDYwNC5tcDQifQ==","uploadAuth":"eyJTZWN1cml0eVRva2VuIjoiQ0FJUzBBUjFxNkZ0NUIyeWZTaklyNVdIRHQ3eW1lY1ozNnVQVGtmVW9Ib0dZY2hKdHZUYW9UejJJSGhKZVhOdkJPMGV0ZjQrbVdCWTdQY1lsclVxRU1BYkhSR2RONUV0dHNvS3JGejZKcGZadjh1ODRZQURpNUNqUWI4ZzBNa0dtcDI4V2Y3d2FmK0FVQlhHQ1RtZDVNTVlvOWJUY1RHbFFDWnVXLy90b0pWN2I5TVJjeENsWkQ1ZGZybC9MUmRqcjhsbzF4R3pVUEcyS1V6U24zYjNCa2hsc1JZZTcyUms4dmFIeGRhQXpSRGNnVmJtcUpjU3ZKK2pDNEM4WXM5Z0c1MTlYdHlwdm9weGJiR1Q4Q05aNXo5QTlxcDlrTTQ5L2l6YzdQNlFIMzViNFJpTkw4L1o3dFFOWHdoaWZmb2JIYTlZcmZIZ21OaGx2dkRTajQzdDF5dFZPZVpjWDBha1E1dTdrdTdaSFArb0x0OGphWXZqUDNQRTNyTHBNWUx1NFQ0OFpYVVNPRHREWWNaRFVIaHJFazRSVWpYZEk2T2Y4VXJXU1FDN1dzcjIxN290ZzdGeXlrM3M4TWFIQWtXTFg3U0IyRHdFQjRjNGFFb2tWVzRSeG5lelc2VUJhUkJwYmxkN0JxNmNWNWxPZEJSWm9LK0t6UXJKVFg5RXoycExtdUQ2ZS9MT3M3b0RWSjM3V1p0S3l1aDRZNDlkNFU4clZFalBRcWl5a1QwcEZncGZUSzFSemJQbU5MS205YmFCMjUvelcrUGREZTBkc1Znb0lGS09waUdXRzNSTE5uK3p0Sjl4YmtlRStzS1V3cUtUcnNzL1RnSjB0NDlXVkZpSWVOODE4MUErdS9Mc3RCbksrNzY1RHk3dDVYUi91UHVncHRVUXN4UThJNjM3MmJiQzVtNlA0a2I5Ty9kcHhKM2xQMFIwV2dteWRuQkR4L1NmdTJrS3ZSaHBrUnZ2WTB0Q3NRdk1pRDdySnB4R2dxelJseWxlZm81WG1QWEZUUW1uOGw1cEFNbXkvNjB4WHVkdmJDakgxMHA2V0tjREdvQUJGY0hkbVZEclgrTEtmNUhNVUpJSE1oVGwwQnFsdy83eDg0MVlOTmZYOXVIWkFGUWV6NCtraWx5Y0hvRW9RUnVyWmYrWEg3TmxFeFhWUU96YTkrVzN6bExHemxBblBoRzhkUi9iYnllQkZVOHlwa0NOTFVuZzBnQWxlRFhvcnRSc3RTMXJjRGpkOWk1RmpLMWRnVVlYSVQ4R0tFOEVHS1dDWCtjK1JlV1JMYXc9IiwiQWNjZXNzS2V5SWQiOiJTVFMuTlYyRWRGdDg4aGlkTGFlRHpTbUdlWTFxQyIsIkV4cGlyZVVUQ1RpbWUiOiIyMDIwLTA0LTA4VDAzOjU0OjM3WiIsIkFjY2Vzc0tleVNlY3JldCI6IkdkdFJUcDhGUmZEYnV0RDVFZktDUFY2SjdSa0xrUnp0a3RXWVo0dW1CMlRrIiwiRXhwaXJhdGlvbiI6IjM0NTEiLCJSZWdpb24iOiJjbi1zaGFuZ2hhaSJ9","videoId":"2095462bcdd447c0a0fe8749bf72c161"}
     */

    private String fileName;
    private String imageType;
    private ImageUploadAuthBean imageUploadAuth;
    private String question;
    private String title;
    private VideoUploadAuthBean videoUploadAuth;

    public SvOptions() {
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public ImageUploadAuthBean getImageUploadAuth() {
        return imageUploadAuth;
    }

    public void setImageUploadAuth(ImageUploadAuthBean imageUploadAuth) {
        this.imageUploadAuth = imageUploadAuth;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VideoUploadAuthBean getVideoUploadAuth() {
        return videoUploadAuth;
    }

    public void setVideoUploadAuth(VideoUploadAuthBean videoUploadAuth) {
        this.videoUploadAuth = videoUploadAuth;
    }

    public static class ImageUploadAuthBean implements Parcelable {
        /**
         * imageId : 90e54fcf199e4fefb6da3259615c22c5
         * imageURL : https://outin-ff75076a297a11eaa04d00163e1c9256.oss-cn-shanghai.aliyuncs.com/image/cover/4A6B027F34B44F2FB8C42DEF5C82FCAB-6-2.png?Expires=1586318226&OSSAccessKeyId=LTAIxSaOfEzCnBOj&Signature=w6XVvUbKni6aLo5Pn%2F9g9fQF0b4%3D
         * uploadAddress : eyJFbmRwb2ludCI6Imh0dHBzOi8vb3NzLWNuLXNoYW5naGFpLmFsaXl1bmNzLmNvbSIsIkJ1Y2tldCI6Im91dGluLWZmNzUwNzZhMjk3YTExZWFhMDRkMDAxNjNlMWM5MjU2IiwiRmlsZU5hbWUiOiJpbWFnZS9jb3Zlci80QTZCMDI3RjM0QjQ0RjJGQjhDNDJERUY1QzgyRkNBQi02LTIucG5nIn0=
         * uploadAuth : eyJTZWN1cml0eVRva2VuIjoiQ0FJUzB3UjFxNkZ0NUIyeWZTaklyNWJHUDR6YWkrbDcxZnJjUkhMVW5sbG5Qclo1bjZxZDBEejJJSGhKZVhOdkJPMGV0ZjQrbVdCWTdQY1lsclVxRU1BYkhSR2RONUV0dHNvS3JGejZKcGZadjh1ODRZQURpNUNqUWRNaTBNa0dtcDI4V2Y3d2FmK0FVQkxHQ1RtZDVNQVlvOWJUY1RHbFFDWnVXLy90b0pWN2I5TVJjeENsWkQ1ZGZybC9MUmRqcjhsbzF4R3pVUEcyS1V6U24zYjNCa2hsc1JZZTcyUms4dmFIeGRhQXpSRGNnVmJtcUpjU3ZKK2pDNEM4WXM5Z0c1MTlYdHlwdm9weGJiR1Q4Q05aNXo5QTlxcDlrTTQ5L2l6YzdQNlFIMzViNFJpTkw4L1o3dFFOWHdoaWZmb2JIYTlZcmZIZ21OaGx2dkRTajQzdDF5dFZPZVpjWDBha1E1dTdrdTdaSFArb0x0OGphWXZqUDNQRTNyTHBNWUx1NFQ0OFpYVVNPRHREWWNaRFVIaHJFazRSVWpYZEk2T2Y4VXJXU1FDN1dzcjIxN290ZzdGeXlrM3M4TWFIQWtXTFg3U0IyRHdFQjRjNGFFb2tWVzRSeG5lelc2VUJhUkJwYmxkN0JxNmNWNWxPZEJSWm9LK0t6UXJKVFg5RXoycExtdUQ2ZS9MT3M3b0RWSjM3V1p0S3l1aDRZNDlkNFU4clZFalBRcWl5a1Qwa0ZncGZUSzFSemJQbU5MS205YmFCMjUvelcrUGREZTBkc1Znb0pWS0RwaUdXRzNSTE5uK3p0Sjl4YmtlRStzS1V3cUtUcnNzL1RnSjB0NDlXVkZpSWVOODE4MUErdS9Mc3RCbksrNzY1RHk3dC8yOHg5ZFNmdmFzM3NCYzdKS2o4M3JYTjVHZU81Q3pCUDVOVXdwbUhCRGRkSmoyc1lHRjh6ZnlvZ1hZS21nc01pV25jT1d4RXN3bk1qem50SVpWQWlLM1dsaU1lVS8xSjVjM2NTaWE5K0Z0bkJlbUE2cTB3UmZoWWUrUkRRajQxV0wydlExdU5Hb0FCVTdmWlNWbVVBTzhjZ2RiLzc2cXJib04zZUJIVnpYcHpnbmxqRmtBTXErZmxzVjl2d2Z2VG1hSzJoOTJYbVJZL0F1TE80QXdCQmFhTnpZbTBpM29GNEN1UTcyQVBldjFiVDlHM3pmcjd4Uk9MN0xMai9PTzN4NHlHS0N1VmZvOVFKcWJKcXgydVhNeitoQm1nL3hWSGhFT0MyQWRGb2kvNEFDZmpXM1FiSFFFPSIsIkFjY2Vzc0tleUlkIjoiU1RTLk5Vc3Q2bmY2WmI4N0ZUZXpZMjI5VXBvNjIiLCJFeHBpcmVVVENUaW1lIjoiMjAyMC0wNC0wOFQwMzo1NDozN1oiLCJBY2Nlc3NLZXlTZWNyZXQiOiJEUEFqYmVZMzhwcXM4WWFjOTd3NUxhMzR0R3l5S25GWkxlU0x1NGY4M2tQQiIsIkV4cGlyYXRpb24iOiIzNDUxIiwiUmVnaW9uIjoiY24tc2hhbmdoYWkifQ==
         */

        private String imageId;
        private String imageURL;
        private String uploadAddress;
        private String uploadAuth;

        public ImageUploadAuthBean() {
        }


        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getUploadAddress() {
            return uploadAddress;
        }

        public void setUploadAddress(String uploadAddress) {
            this.uploadAddress = uploadAddress;
        }

        public String getUploadAuth() {
            return uploadAuth;
        }

        public void setUploadAuth(String uploadAuth) {
            this.uploadAuth = uploadAuth;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.imageId);
            dest.writeString(this.imageURL);
            dest.writeString(this.uploadAddress);
            dest.writeString(this.uploadAuth);
        }

        protected ImageUploadAuthBean(Parcel in) {
            this.imageId = in.readString();
            this.imageURL = in.readString();
            this.uploadAddress = in.readString();
            this.uploadAuth = in.readString();
        }

        public static final Creator<ImageUploadAuthBean> CREATOR = new Creator<ImageUploadAuthBean>() {
            @Override
            public ImageUploadAuthBean createFromParcel(Parcel source) {
                return new ImageUploadAuthBean(source);
            }

            @Override
            public ImageUploadAuthBean[] newArray(int size) {
                return new ImageUploadAuthBean[size];
            }
        };
    }

    public static class VideoUploadAuthBean implements Parcelable{
        public VideoUploadAuthBean() {
        }



        /**
         * uploadAddress : eyJFbmRwb2ludCI6Imh0dHBzOi8vb3NzLWNuLXNoYW5naGFpLmFsaXl1bmNzLmNvbSIsIkJ1Y2tldCI6Im91dGluLWZmNzUwNzZhMjk3YTExZWFhMDRkMDAxNjNlMWM5MjU2IiwiRmlsZU5hbWUiOiJzdi80ZTU4NDdiMi0xNzE1N2I4NDYwNC80ZTU4NDdiMi0xNzE1N2I4NDYwNC5tcDQifQ==
         * uploadAuth : eyJTZWN1cml0eVRva2VuIjoiQ0FJUzBBUjFxNkZ0NUIyeWZTaklyNVdIRHQ3eW1lY1ozNnVQVGtmVW9Ib0dZY2hKdHZUYW9UejJJSGhKZVhOdkJPMGV0ZjQrbVdCWTdQY1lsclVxRU1BYkhSR2RONUV0dHNvS3JGejZKcGZadjh1ODRZQURpNUNqUWI4ZzBNa0dtcDI4V2Y3d2FmK0FVQlhHQ1RtZDVNTVlvOWJUY1RHbFFDWnVXLy90b0pWN2I5TVJjeENsWkQ1ZGZybC9MUmRqcjhsbzF4R3pVUEcyS1V6U24zYjNCa2hsc1JZZTcyUms4dmFIeGRhQXpSRGNnVmJtcUpjU3ZKK2pDNEM4WXM5Z0c1MTlYdHlwdm9weGJiR1Q4Q05aNXo5QTlxcDlrTTQ5L2l6YzdQNlFIMzViNFJpTkw4L1o3dFFOWHdoaWZmb2JIYTlZcmZIZ21OaGx2dkRTajQzdDF5dFZPZVpjWDBha1E1dTdrdTdaSFArb0x0OGphWXZqUDNQRTNyTHBNWUx1NFQ0OFpYVVNPRHREWWNaRFVIaHJFazRSVWpYZEk2T2Y4VXJXU1FDN1dzcjIxN290ZzdGeXlrM3M4TWFIQWtXTFg3U0IyRHdFQjRjNGFFb2tWVzRSeG5lelc2VUJhUkJwYmxkN0JxNmNWNWxPZEJSWm9LK0t6UXJKVFg5RXoycExtdUQ2ZS9MT3M3b0RWSjM3V1p0S3l1aDRZNDlkNFU4clZFalBRcWl5a1QwcEZncGZUSzFSemJQbU5MS205YmFCMjUvelcrUGREZTBkc1Znb0lGS09waUdXRzNSTE5uK3p0Sjl4YmtlRStzS1V3cUtUcnNzL1RnSjB0NDlXVkZpSWVOODE4MUErdS9Mc3RCbksrNzY1RHk3dDVYUi91UHVncHRVUXN4UThJNjM3MmJiQzVtNlA0a2I5Ty9kcHhKM2xQMFIwV2dteWRuQkR4L1NmdTJrS3ZSaHBrUnZ2WTB0Q3NRdk1pRDdySnB4R2dxelJseWxlZm81WG1QWEZUUW1uOGw1cEFNbXkvNjB4WHVkdmJDakgxMHA2V0tjREdvQUJGY0hkbVZEclgrTEtmNUhNVUpJSE1oVGwwQnFsdy83eDg0MVlOTmZYOXVIWkFGUWV6NCtraWx5Y0hvRW9RUnVyWmYrWEg3TmxFeFhWUU96YTkrVzN6bExHemxBblBoRzhkUi9iYnllQkZVOHlwa0NOTFVuZzBnQWxlRFhvcnRSc3RTMXJjRGpkOWk1RmpLMWRnVVlYSVQ4R0tFOEVHS1dDWCtjK1JlV1JMYXc9IiwiQWNjZXNzS2V5SWQiOiJTVFMuTlYyRWRGdDg4aGlkTGFlRHpTbUdlWTFxQyIsIkV4cGlyZVVUQ1RpbWUiOiIyMDIwLTA0LTA4VDAzOjU0OjM3WiIsIkFjY2Vzc0tleVNlY3JldCI6IkdkdFJUcDhGUmZEYnV0RDVFZktDUFY2SjdSa0xrUnp0a3RXWVo0dW1CMlRrIiwiRXhwaXJhdGlvbiI6IjM0NTEiLCJSZWdpb24iOiJjbi1zaGFuZ2hhaSJ9
         * videoId : 2095462bcdd447c0a0fe8749bf72c161
         */



        private String uploadAddress;
        private String uploadAuth;
        private String videoId;

        public String getUploadAddress() {
            return uploadAddress;
        }

        public void setUploadAddress(String uploadAddress) {
            this.uploadAddress = uploadAddress;
        }

        public String getUploadAuth() {
            return uploadAuth;
        }

        public void setUploadAuth(String uploadAuth) {
            this.uploadAuth = uploadAuth;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.uploadAddress);
            dest.writeString(this.uploadAuth);
            dest.writeString(this.videoId);
        }

        protected VideoUploadAuthBean(Parcel in) {
            this.uploadAddress = in.readString();
            this.uploadAuth = in.readString();
            this.videoId = in.readString();
        }

        public static final Creator<VideoUploadAuthBean> CREATOR = new Creator<VideoUploadAuthBean>() {
            @Override
            public VideoUploadAuthBean createFromParcel(Parcel source) {
                return new VideoUploadAuthBean(source);
            }

            @Override
            public VideoUploadAuthBean[] newArray(int size) {
                return new VideoUploadAuthBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileName);
        dest.writeString(this.imageType);
        dest.writeParcelable(this.imageUploadAuth, flags);
        dest.writeString(this.question);
        dest.writeString(this.title);
        dest.writeParcelable(this.videoUploadAuth, flags);
    }

    protected SvOptions(Parcel in) {
        this.fileName = in.readString();
        this.imageType = in.readString();
        this.imageUploadAuth = in.readParcelable(ImageUploadAuthBean.class.getClassLoader());
        this.question = in.readString();
        this.title = in.readString();
        this.videoUploadAuth = in.readParcelable(VideoUploadAuthBean.class.getClassLoader());
    }

    public static final Creator<SvOptions> CREATOR = new Creator<SvOptions>() {
        @Override
        public SvOptions createFromParcel(Parcel source) {
            return new SvOptions(source);
        }

        @Override
        public SvOptions[] newArray(int size) {
            return new SvOptions[size];
        }
    };
}
