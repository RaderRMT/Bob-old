package fr.rader.bob.protocol;

import com.google.gson.Gson;
import fr.rader.bob.ReplayWriter;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ProtocolVersion {

    private static final List<ProtocolVersion> versionList = new ArrayList<>();

    private String name;
    private int version;
    private Set<String> includedVersions;

    private static ProtocolVersion currentProtocol;

    public static final ProtocolVersion v1_7_6 = register(5, "1.7.6-1.7.10", new VersionRange("1.7", 6, 10));
    public static final ProtocolVersion v1_8 = register(47, "1.8.x");
    public static final ProtocolVersion v1_9 = register(107, "1.9");
    public static final ProtocolVersion v1_9_1 = register(108, "1.9.1");
    public static final ProtocolVersion v1_9_2 = register(109, "1.9.2");
    public static final ProtocolVersion v1_9_3 = register(110, "1.9.3/4", new VersionRange("1.9", 3, 4));
    public static final ProtocolVersion v1_10 = register(210, "1.10.x");
    public static final ProtocolVersion v1_11 = register(315, "1.11");
    public static final ProtocolVersion v1_11_1 = register(316, "1.11.1/2", new VersionRange("1.11", 1, 2));
    public static final ProtocolVersion v1_12 = register(335, "1.12");
    public static final ProtocolVersion v1_12_1 = register(338, "1.12.1");
    public static final ProtocolVersion v1_12_2 = register(340, "1.12.2");
    public static final ProtocolVersion v1_14 = register(477, "1.14");
    public static final ProtocolVersion v1_14_1 = register(480, "1.14.1");
    public static final ProtocolVersion v1_14_2 = register(485, "1.14.2");
    public static final ProtocolVersion v1_14_3 = register(490, "1.14.3");
    public static final ProtocolVersion v1_14_4 = register(498, "1.14.4");
    public static final ProtocolVersion v1_15 = register(573, "1.15");
    public static final ProtocolVersion v1_15_1 = register(575, "1.15.1");
    public static final ProtocolVersion v1_15_2 = register(578, "1.15.2");
    public static final ProtocolVersion v1_16 = register(735, "1.16");
    public static final ProtocolVersion v1_16_1 = register(736, "1.16.1");
    public static final ProtocolVersion v1_16_2 = register(751, "1.16.2");
    public static final ProtocolVersion v1_16_3 = register(753, "1.16.3");
    public static final ProtocolVersion v1_16_4 = register(754, "1.16.4");
    public static final ProtocolVersion unknown = register(-1, "UNKNOWN");

    private static ProtocolVersion register(int version, String name) {
        return register(version, name, null);
    }

    private static ProtocolVersion register(int version, String name, VersionRange range) {
        ProtocolVersion protocol = new ProtocolVersion(version, name, range);
        versionList.add(protocol);
        return protocol;
    }

    public static ProtocolVersion getClosest(float protocol) {
        for (ProtocolVersion version : versionList) {
            if(version.getVersion() == protocol) return version;
        }

        return null;
    }

    // since
    public static boolean atLeast(ProtocolVersion version) {
        return currentProtocol.getVersion() >= version.getVersion();
    }

    // until
    public static boolean atMost(ProtocolVersion version) {
        return currentProtocol.getVersion() <= version.getVersion();
    }

    public ProtocolVersion(int version, String name, VersionRange range) {
        this.version = version;
        this.name = name;

        if(range != null) {
            includedVersions = new HashSet<>();

            for(int i = range.getFrom(); i <= range.getTo(); i++) {
                if (i == 0) {
                    includedVersions.add(range.getBaseVersion());
                }

                includedVersions.add(range.getBaseVersion() + "." + i);
            }
        }
    }

    public boolean isRange() {
        return includedVersions.size() != 1;
    }

    public static ProtocolVersion getCurrentProtocol() {
        return currentProtocol;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public Set<String> getIncludedVersions() {
        return includedVersions;
    }

    public static void determineProtocol(File replay) {
        try {
            ZipFile mcprFile = new ZipFile(replay);
            mcprFile.extractFile("metaData.json", ReplayWriter.REPLAY_RECORDINGS + "bob\\");

            Gson gson = new Gson();

            Map<?, ?> map = gson.fromJson(new FileReader(new File(ReplayWriter.REPLAY_RECORDINGS + "bob\\metaData.json")), Map.class);

            currentProtocol = getClosest(Float.parseFloat(String.valueOf(map.get("protocol"))));
        } catch (ZipException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
