package com.rz.core;

import com.rz.core.excpetion.AggregateException;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class RZHelper {
    private final static byte[] PASSWORD_KEY = new byte[]{'h', 'j', 'n', 'o', 't', 'i', 'f', 'y'};
    private final static byte[] PASSWORD_IV = new byte[]{'h', 'j', 't', 'p', 'm', 's', 'g', 's'};
    private static List<String> ipV4s = null;
    public final static Set<String> baseMethodNames =
            new HashSet<>(Arrays.asList("getClass", "toString", "equals", "hashCode", "notify", "notifyAll", "wait"));
    private final static List<String> dateFormats = Arrays.asList(
            "d MMM yyyy HH:mm:ss 'GMT'",
            "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss.SSSZ",
            "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss.SSS",
            "yyyy/MM/dd HH:mm:ss.SSSZ");

    public static boolean isBaseClazz(Class<?> clazz) {
        Assert.isNotNull(clazz, "clazz");

        if (clazz.isPrimitive() || String.class == clazz || Integer.class == clazz || Boolean.class == clazz || Character.class == clazz || Byte.class == clazz
                || Short.class == clazz || Long.class == clazz || Float.class == clazz || Double.class == clazz || Void.class == clazz) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean interfaceOf(Class<?> clazz, Class<?> targetInterfaceClass) {
        Assert.isNotNull(clazz, "clazz");
        Assert.isNotNull(targetInterfaceClass, "targetInterfaceClass");

        Class<?>[] interfaceClasses = clazz.getInterfaces();
        for (int i = 0; i < interfaceClasses.length; i++) {
            if (targetInterfaceClass.equals(interfaceClasses[i])) {
                return true;
            } else {
                if (interfaceOf(interfaceClasses[i], targetInterfaceClass)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isEmptyCollection(Collection collection) {
        if (null == collection || collection.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyCollection(Map map) {
        if (null == map || map.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyCollection(Object[] array) {
        if (null == array) {
            return true;
        }

        return 0 == array.length;
    }

    public static boolean isEmptyCollection(boolean[] array) {
        if (null == array) {
            return true;
        }

        return 0 == array.length;
    }

    public static boolean isEmptyCollection(int[] array) {
        if (null == array || 0 == array.length) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyCollection(double[] array) {
        if (null == array || 0 == array.length) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyCollection(byte[] array) {
        if (null == array || 0 == array.length) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyCollection(char[] array) {
        if (null == array || 0 == array.length) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyCollection(float[] array) {
        if (null == array || 0 == array.length) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyCollection(long[] array) {
        if (null == array || 0 == array.length) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmptyCollection(short[] array) {
        if (null == array || 0 == array.length) {
            return true;
        } else {
            return false;
        }
    }

    public static void safeClose(Closeable instance) {
        Assert.isNotNull(instance, "instance");

        try {
            instance.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Boolean> asList(boolean[] array) {
        if (null == array) {
            return null;
        }

        return Arrays.asList(ArrayUtils.toObject(array));
    }

    public static List<Integer> asList(int[] array) {
        if (null == array) {
            return null;
        }

        return Arrays.asList(ArrayUtils.toObject(array));
    }

    public static List<Double> asList(double[] array) {
        if (null == array) {
            return null;
        }

        return Arrays.asList(ArrayUtils.toObject(array));
    }

    public static List<Byte> asList(byte[] array) {
        if (null == array) {
            return null;
        }

        return Arrays.asList(ArrayUtils.toObject(array));
    }

    public static List<Character> asList(char[] array) {
        if (null == array) {
            return null;
        }

        return Arrays.asList(ArrayUtils.toObject(array));
    }

    public static List<Float> asList(float[] array) {
        if (null == array) {
            return null;
        }

        return Arrays.asList(ArrayUtils.toObject(array));
    }

    public static List<Long> asList(long[] array) {
        if (null == array) {
            return null;
        }

        return Arrays.asList(ArrayUtils.toObject(array));
    }

    public static List<Short> asList(short[] array) {
        if (null == array) {
            return null;
        }

        return Arrays.asList(ArrayUtils.toObject(array));
    }

    public static String encrypt(String value) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Assert.isNotBlank(value, "value");

        KeySpec keySpec = new DESKeySpec(PASSWORD_KEY);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(PASSWORD_IV);
        SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        byte[] bytes = cipher.doFinal(value.getBytes(StandardCharsets.US_ASCII));

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (2 > hex.length()) {
                // b -> 0b
                stringBuilder.append(0);
            }
            stringBuilder.append(hex);
        }

        return stringBuilder.toString();
    }

    public static String decrypt(String value) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Assert.isNotBlank(value, "value");

        KeySpec keySpec = new DESKeySpec(PASSWORD_KEY);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(PASSWORD_IV);
        SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] bytes = cipher.doFinal(RZHelper.hexStringToByte(value));

        return new String(bytes, StandardCharsets.US_ASCII);
    }

    public static byte[] hexStringToByte(String hexString) {
        Assert.isNotBlank(hexString, "hexString");
        Assert.isTrue(0 == hexString.length() % 2, "hexString.length() % 2");

        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] chars = hexString.toCharArray();
        byte[] bytes = new byte[length];

        for (int i = 0; i < length; i++) {
            bytes[i] = (byte) ("0123456789ABCDEF".indexOf(chars[i * 2]) << 4 | "0123456789ABCDEF".indexOf(chars[i * 2 + 1]));
        }

        return bytes;
    }

    public static int getBKDRHashCode(String value) {
        if (null == value) {
            return 0;
        }

        int seed = 131; // 31 131 1313 13131 131313 etc..
        int hashCode = 0;

        for (int i = 0; i < value.length(); i++) {
            hashCode = hashCode * seed + (int) value.charAt(i);
        }

        return (int) (hashCode & 0x7FFFFFFF);
    }

    public static String getIpV4() throws SocketException {
        String defaultIpV4 = "127.0.0.1";
        if (null != RZHelper.ipV4s) {
            return RZHelper.ipV4s.stream().filter(o -> !o.equals(defaultIpV4)).findFirst().orElse(defaultIpV4);
        }

        List<String> hostAddresses = new ArrayList<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress inetAddress;
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                inetAddress = inetAddresses.nextElement();
                if (null != inetAddress && inetAddress instanceof Inet4Address) {
                    hostAddresses.add(inetAddress.getHostAddress());
                }
            }
        }
        RZHelper.ipV4s = hostAddresses;

        return RZHelper.ipV4s.stream().filter(o -> !o.equals(defaultIpV4)).findFirst().orElse(defaultIpV4);
    }

    @SuppressWarnings("rawtypes")
    public static Map<String, Class<?>> getGenericParameterBaseClasses(Class<?> clazz) {
        Map<String, Class<?>> map = new HashMap<>();
        for (TypeVariable typeVariable : clazz.getTypeParameters()) {
            for (Type bound : typeVariable.getBounds()) {
                map.put(typeVariable.getName(), (Class<?>) bound);
            }
        }

        return map;
    }

    public static <T> List<T> iteratorToList(Iterator<T> iterator) {
        if (null == iterator) {
            return null;
        }

        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        return list;
    }

    public static <T> List<T> collectionToList(Collection<T> collection) {
        if (null == collection) {
            return null;
        }

        List<T> list;
        if (collection instanceof List) {
            list = (List) collection;
        } else {
            list = new ArrayList(collection);
        }

        return list;
    }

    public static <T> List<T> setToList(Set<T> set) {
        if (null == set) {
            return null;
        }

        return new ArrayList<>(set);
    }

    public static <T> Set<T> setToList(List<T> list) {
        if (null == list) {
            return null;
        }

        return new HashSet<>(list);
    }

    public static Field[] getDeclaredFields(Class<?> clazz) {
        if (null == clazz) {
            return null;
        }

        Map<String, Field> allFields = new HashMap<>();
        Class<?> superClass = clazz;
        do {
            Field[] fields = superClass.getDeclaredFields();
            for (Field field : fields) {
                if (!allFields.containsKey(field.getName())) {
                    allFields.put(field.getName(), field);
                }
            }

            superClass = superClass.getSuperclass();
        } while (null != superClass);

        return allFields.values().toArray(new Field[allFields.size()]);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        // ConcurrentHashMap's putIfAbsent is thread safety
        // failed to put then return null, or return value
        return o -> null == map.putIfAbsent(keyExtractor.apply(o), Boolean.TRUE);
    }

    public static Object toObject(Class clazz, String value) {
        Assert.isNotNull(clazz, "clazz");

        if (boolean.class.equals(clazz) || Boolean.class.equals(clazz)) {
            return Boolean.valueOf(value);
        } else if (short.class.equals(clazz) || Short.class.equals(clazz)) {
            return Short.valueOf(value);
        } else if (int.class.equals(clazz) || Integer.class.equals(clazz)) {
            return Integer.valueOf(value);
        } else if (long.class.equals(clazz) || Long.class.equals(clazz)) {
            return Long.valueOf(value);
        } else if (float.class.equals(clazz) || Float.class.equals(clazz)) {
            return Float.valueOf(value);
        } else if (double.class.equals(clazz) || Double.class.equals(clazz)) {
            return Double.valueOf(value);
        } else if (char.class.equals(clazz) || Character.class.equals(clazz)) {
            return value.charAt(0);
        } else if (byte.class.equals(clazz) || Byte.class.equals(clazz)) {
            return Byte.valueOf(value);
        } else if (String.class.equals(clazz)) {
            return value;
        } else if (clazz.isEnum()) {
            return Enum.valueOf(clazz, value);
        } else if (Date.class.equals(clazz)) {
            List<String> dateFormats = RZHelper.dateFormats;
            for (String dateFormat : dateFormats) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
                try {
                    return simpleDateFormat.parse(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            try {
                return new Date(Long.valueOf(value));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Cannot convert to date with default date format.");
            }
        }

        throw new IllegalArgumentException(String.format("Cannot resolve class(%s).", clazz.getName()));
    }

    public static void saftSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void saftClose(Closeable closeable) {
        if (null == closeable) {
            return;
        }
        try {
            closeable.close();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static int getCurrentProcessId() {
        return Integer.valueOf(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    }

    public static String formatExceptionMessage(Throwable throwable) {
        if (null == throwable) {
            return "";
        }
        if (null == throwable.getCause()) {
            return throwable.getMessage();
        }

        return RZHelper.formatExceptionMessage(throwable, "", "");
    }

    private static String formatExceptionMessage(Throwable throwable, String message, String layer) {
        if (null == throwable) {
            return message;
        }

        message = null == message ? "" : message;
        layer = null == layer ? "" : layer;
        message += layer + (null == throwable.getMessage() ? "" : throwable.getMessage()) + "\r\n";
        layer = "----" + layer;
        if (throwable instanceof AggregateException) {
            AggregateException aggregateException = (AggregateException) throwable;
            if (null != aggregateException.getCauses()) {
                for (Throwable cause : aggregateException.getCauses()) {
                    message = RZHelper.formatExceptionMessage(cause, message, layer);
                }
            }
        } else {
            if (null != throwable.getCause()) {
                message = RZHelper.formatExceptionMessage(throwable.getCause(), message, layer);
            }
        }

        return message;
    }

    public static List<Class> getClassesByPackage(ClassLoader classLoader, String packageName) throws IOException {
        Assert.isNotNull(classLoader, "classLoader");
        Assert.isNotBlank(packageName, "packageName");

        List<Class> classes = new ArrayList<>();
        URL url = classLoader.getResource(packageName.replace('.', '/'));
        if (null == url) {
            return null;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((InputStream) url.getContent()));
        String line;
        while (null != (line = bufferedReader.readLine())) {
            if (line.endsWith(".class")) {
                try {
                    String className = packageName + "." + line.substring(0, line.length() - ".class".length());
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return classes;
    }
}
