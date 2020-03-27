package com.bird.spring.boot.redis.validator;


import com.bird.spring.boot.redis.exception.AddressValidationException;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 服务器节点校验
 *
 * @author youly
 * 2019/1/7 17:30
 */
public class AddressValidator {

    private static final String ADDRESS_PATTERN = "redis://{0}:{1}";
    private static final MessageFormat FORMAT = new MessageFormat(ADDRESS_PATTERN);

    /**
     * 地址校验正则表达式
     */
    private static final String ADDRESS_REGEX = "^((?<protocol>redis)://)?(?<address>([0-9]{1,3}\\.){3}[0-9]{1,3}):(?<port>[0-9]+)$";
    private static final Pattern PATTERN = Pattern.compile(ADDRESS_REGEX);

    private static final String VAR_KEY_ADDRESS = "address";
    private static final String VAR_KEY_PORT = "port";

    public static String validate(String nodeAddress) {
        Matcher matcher = PATTERN.matcher(nodeAddress);
        if (!matcher.find()) {
            throw new AddressValidationException(nodeAddress + " FORMAT error. eg: redis://127.0.0.1:6379 or 127.0.0.1:6379");
        }
        String address = matcher.group(VAR_KEY_ADDRESS);
        String port = matcher.group(VAR_KEY_PORT);
        return FORMAT.format(new String[]{address, port});
    }

}
