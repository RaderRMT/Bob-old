package fr.rader.bob;

import fr.rader.bob.packets.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private List<Packet> packets = new ArrayList<>();

    private void start() {
        try {
            File replay = new File("C:\\Users\\marti\\Desktop\\recording.tmcpr");
            DataReader reader = new DataReader(Files.readAllBytes(replay.toPath()));

            for(;;) {
                int timestamp = reader.readInt();
                int size = reader.readInt();

                PacketHeader header = new PacketHeader(timestamp, size);

                System.out.print(Integer.toHexString(reader.getOffset()) + ", ");

                byte[] data = reader.readFollowingBytes(size);

                System.out.println(Integer.toHexString(data[0]));

                switch(data[0]) {
                    case 0x02:
                        packets.add(new P02LoginSuccess(header, data));
                        break;
                    case 0x0d:
                        packets.add(new P0DServerDifficulty(header, data));
                        break;
                    case 0x17:
                        packets.add(new P17PluginMessage(header, data));
                        break;
                    case 0x1c:
                        packets.add(new P1CUnloadChunk(header, data));
                        break;
                    case 0x24:
                        packets.add(new P24JoinGame(header, data));
                        break;
                    case 0x30:
                        packets.add(new P30PlayerAbilities(header, data));
                        break;
                    case 0x3f:
                        packets.add(new P3FHeldItemChange(header, data));
                        break;
                    case 0x5a:
                        packets.add(new P5ADeclareRecipes(header, data));
                        break;
                    default:
                        System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        start();
    }
}
