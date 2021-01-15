package fr.rader.bob;

public class OS {

    private static final String osName;

    public static String getBobFolder() {
        if(BobSettings.getWorkingDirectory() == null) {
            if(getOS().equals("windows")) return System.getenv("userprofile") + "/.bob/";
            else if(getOS().equals("*nix") ||
                    getOS().equals("mac")) return "~/.bob/";

            return null;
        } else {
            return BobSettings.getWorkingDirectory();
        }
    }

    public static String getOS() {
        if(osName.contains("windows")) return "windows";
        else if(osName.contains("mac")) return "mac";
        else if(osName.contains("nix") ||
                osName.contains("nux") ||
                osName.contains("aix")) return "*nix";

        return null;
    }

    public static String getMinecraftFolder() {
        switch(getOS()) {
            case "windows":
                return System.getenv("appdata") + "/.minecraft/";
            case "*nix":
                return "~/.minecraft/";
            case "mac":
                return "~/Library/Application Support/minecraft/";
        }

        return null;
    }

    static {
        osName = System.getProperty("os.name").toLowerCase();
    }
}
