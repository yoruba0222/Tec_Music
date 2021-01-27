import java.io.*;

public class Main {

          private static int[] MusicScale = {191, 170, 151, 143, 127, 113, 101, 0};
          private static String[] ScaleName = {"do", "re", "mi", "fa", "so", "la", "si","mu"};

          public static void main(String[]args) {

                    BufferedReader br = null;
                    FileWriter filewriter = null;
                    String file_name_in, file_name_out = "Assets/template.t7";

                    // decide file name and score name in here 
                    try {
                              br = null;
                              file_name_out = "Assets/template.t7";
                              file_name_in = args[2];
                    } catch(IndexOutOfBoundsException e) {
                              file_name_in = "Scores/Score.csv";
                    }

                    System.out.println(file_name_in);

                    try {
                              File readfile = new File(file_name_in);
                              File writefile = new File(file_name_out);
                              filewriter = new FileWriter(file_name_out, true);
                              br = new BufferedReader(new FileReader(readfile));

                              filewriter.write("\nSCORE");
                              filewriter.flush();
                              
                              String line;
                              String[] tmp;
                              int[] data;
                              int bpm;
                              int oneLength;
                              int noteLength;

                              line = br.readLine();
                              tmp = line.split(",");

                              bpm = Integer.parseInt(tmp[2].substring(4, tmp[2].length()-1));

                              //System.out.println(bpm);

                              oneLength = (int)(100000 * ((1.21*bpm) / 170));

                              log(oneLength);

                              while ((line = br.readLine()) != null) {

                                        int index;

                                        tmp = line.split(",");
                                        data = new int[2];

                                        index = getIndex(ScaleName, tmp[1].substring(0, 2));

                                        // normal music scale
                                        data[1] = MusicScale[index];
                                        // judge half note
                                        if (tmp[1].contains("1")) {
                                                  if (index != 6) {
                                                            data[1] -= (MusicScale[index]-MusicScale[index+1]) / 2;
                                                  }
                                        } else if (tmp[1].contains("2")) {
                                                  if (index != 0) {
                                                            data[1] += (MusicScale[index-1]-MusicScale[index]) / 2;
                                                  }
                                        }

                                        // scale +1
                                        if (tmp[1].contains("+")) {

                                                  data[1] = MusicScale[index] / 2;

                                                  // judge tmp[1] contains '♯' or '♭'
                                                  // ♯
                                                  if (tmp[1].contains("1")) {
                                                            if (index != 6) {
                                                                      data[1] -= (MusicScale[index]/2-MusicScale[index+1]/2) / 2;
                                                            }
                                                  } else if (tmp[1].contains("2")) {
                                                            if (index != 0) {
                                                                      data[1] += (MusicScale[index-1]/2-MusicScale[index]/2) / 2;
                                                            }
                                                  }
                                        }
                                        //// scale -1
                                        //else if (tmp[1].contains("-")) {
                                        //
                                        //          data[1] = MusicScale[index] * 2;
                                        //
                                        //          if (tmp[1].contains("1")) {
                                        //                    if (index != 6) {
                                        //                              data[1] -= (MusicScale[index]*2-MusicScale[index+1]*2) * 2;
                                        //                    }
                                        //          } else if (tmp[1].contains("0")) {
                                        //                    if (index != 0) {
                                        //                              data[1] += (MusicScale[index-1]*2-MusicScale[index]*2) * 2;
                                        //                    }
                                        //          }
                                        //}

                                        if (tmp[0].contains(".")) noteLength = oneLength / (Integer.parseInt(tmp[0].substring(0, tmp[0].length()-1)));
                                        else noteLength = oneLength / (Integer.parseInt(tmp[0].substring(0, tmp[0].length())));
                                        log(noteLength);

                                        // if mute
                                        if (data[1] == 0) {
                                                  data[0] = (int)((98*(noteLength/100000.0)) / 0.5);
                                        } else {
                                                  data[0] = noteLength / (2*data[1]);
                                        }

                                        // if dotted note
                                        if (tmp[0].contains(".")) data[0] = (int)(data[0]*1.5);
                                        
                                        // output

                                        while (true) {

                                                  filewriter.write("\tDC");
                                                  filewriter.flush();

                                                  if (data[0] > 255) {
                                                            //System.out.println("\t255,"+data[1]);
                                                            filewriter.write("\t255,"+data[1]+"\n");
                                                            filewriter.flush();
                                                            data[0] -= 225;
                                                  } else {
                                                            //System.out.println("\t"+data[0]+","+data[1]);
                                                            filewriter.write("\t"+data[0]+","+data[1]+"\n");
                                                            filewriter.flush();
                                                            break;
                                                  }
                                        }
                              
                              }

                    } catch (IOException e) {
                              System.out.println(e.getMessage());
                    } finally {
                              try {
                                        filewriter.close();
                                        br.close();
                              } catch (IOException e) {
                                        System.out.println(e.getMessage());
                              }
                    }
          }
          
          // 配列内に含まれている要素のインデックスを返す
          static int getIndex(String[] array, String str) {
                    int index = 0;
                    
                    for (int i=0; i<array.length; i++) {
                              if (str.equals(array[i])) {
                                        index = i;
                                        break;
                              }
                    }

                    return index;
          }

          // プリントデバッグ
          static void log(Object object) {
                    System.out.println(object);
          }
}