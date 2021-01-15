package org.ruan4261.eiuc.strategy;

import java.util.Objects;

public class ConstructWithFileIdStrategy implements StringResolveStrategy {

    final static String RESULT_PREFIX = "http://oa.sobute.com/weaver/weaver.file.FileDownload?fileid=";

    /**
     * 获取url参数中fileid的值, fileid仅为数值
     */
    @Override
    public String resolve(String origin) {
        Objects.requireNonNull(origin);
        int len = origin.length();
        int idx = origin.indexOf("fileid=");
        if (idx != -1) {
            idx += 7;
            StringBuilder builder = new StringBuilder(6);
            for (; idx < len; idx++) {
                char ch = origin.charAt(idx);
                int num = ch - '0';

                if (num < 0 || num > 9)// end
                    break;

                builder.append(ch);
            }
            return RESULT_PREFIX + builder.toString();
        }
        throw new IllegalArgumentException("Cannot locate 'fileid=' in string: " + origin);
    }

}
