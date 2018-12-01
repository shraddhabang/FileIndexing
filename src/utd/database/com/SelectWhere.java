package utd.database.com;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class SelectWhere {

    Utility utility = Utility.getInstance();

    public SelectWhere() {
    }

    public void selectWhere(String userCommand) {
        try {
            String[] tokens = userCommand.split(" ");
            String tableName = tokens[3].trim();
            if (utility.isTablePresent(tableName, true)) {
                String filter = userCommand.substring(userCommand.indexOf("where") + 5, userCommand.length()).trim();
                String[] filterArray = filter.split("=");
                RandomAccessFile table = new RandomAccessFile(IUtitlityConstants.DATABASE_PATH+
                        File.separator+utility.getSeletedDatabase() + File.separator + tableName + ".tbl",
                        "rw");
                boolean isRecordPresent = false;
                if (table.length() > 0L) {
                    java.util.List<Column> columns = utility.getColumns(tableName);

                    table.readByte();
                    int cells = table.readByte();
                    table.readShort();
                    long rightPointer = table.readInt();
                    ArrayList<Short> cellPointers = new ArrayList<Short>();
                    for (int i = 0; i < cells; i++) {
                        cellPointers.add(Short.valueOf(table.readShort()));
                    }
                    boolean nextPage = true;
                    while (nextPage) {
                        for (int i = 0; i < cellPointers.size(); i++) {
                            table.seek(((Short) cellPointers.get(i)).shortValue());
                            String displayString = "";
                            Boolean isDisplay = Boolean.valueOf(false);
                            for (Column column : columns) {
                                if (column.getDataType().equals("int")) {
                                    String value = "" + table.readInt();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        isDisplay = Boolean.valueOf(true);
                                    }

                                    displayString = displayString + " " + value;
                                } else if (column.getDataType().equals("tinyint")) {
                                    String value = "" + table.readByte();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        isDisplay = Boolean.valueOf(true);
                                    }

                                    displayString = displayString + " " + value;
                                } else if (column.getDataType().equals("smallint")) {
                                    String value = "" + table.readShort();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        isDisplay = Boolean.valueOf(true);
                                    }

                                    displayString = displayString + " " + value;
                                } else if (column.getDataType().equals("bigint")) {
                                    String value = "" + table.readLong();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        isDisplay = Boolean.valueOf(true);
                                    }

                                    displayString = displayString + " " + value;
                                } else if (column.getDataType().equals("real")) {
                                    String value = "" + table.readFloat();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        isDisplay = Boolean.valueOf(true);
                                    }

                                    displayString = displayString + " " + value;
                                } else if (column.getDataType().equals("double")) {
                                    String value = "" + table.readDouble();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        isDisplay = Boolean.valueOf(true);
                                    }

                                    displayString = displayString + " " + value;
                                } else if (column.getDataType().equals("date")) {
                                    String value = utility.convertDateToString(table.readLong());
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        isDisplay = Boolean.valueOf(true);
                                    }

                                    displayString = displayString + " " + value;
                                } else if (column.getDataType().equals("datetime")) {
                                    String value = utility.convertDateTimeToString(table.readLong());
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        isDisplay = Boolean.valueOf(true);
                                    }

                                    displayString = displayString + " " + value;
                                } else {
                                    int length = table.readByte();
                                    byte[] bytes = new byte[length];
                                    table.read(bytes, 0, bytes.length);
                                    String value = new String(bytes);
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        isDisplay = Boolean.valueOf(true);
                                    }

                                    displayString = displayString + " " + value;
                                }
                            }
                                if (isDisplay.booleanValue())
                                {
                                    isRecordPresent = true;
                                    System.out.println(displayString);
                                }
                        }
                        if (rightPointer != 0L) {
                            table.seek(rightPointer);
                            table.readByte();
                            cells = table.readByte();
                            table.readShort();
                            rightPointer = table.readInt();
                            cellPointers = new ArrayList<Short>();
                            for (int i = 0; i < cells; i++) {
                                cellPointers.add(Short.valueOf(table.readShort()));
                            }
                        } else {
                            nextPage = false;
                        }
                    }
                } if(!isRecordPresent){
                    System.out.println("No record present");
                }
                table.close();
            }
        } catch (Exception e) {
            System.out.println("Error, While fectching records from table");
        }
    }

    public boolean isKeyAlreadyPresent(String userCommand) {
        try {
            String[] tokens = userCommand.split(" ");
            String tableName = tokens[3].trim();
            if (utility.isTablePresent(tableName, true)) {
                String filter = userCommand.substring(userCommand.indexOf("where") + 5, userCommand.length()).trim();
                String[] filterArray = filter.split("=");
                RandomAccessFile table = new RandomAccessFile(IUtitlityConstants.DATABASE_PATH+
                        File.separator+utility.getSeletedDatabase() + File.separator + tableName + ".tbl",
                        "rw");
                if (table.length() > 0L) {
                    java.util.List<Column> columns = utility.getColumns(tableName);

                    table.readByte();
                    int cells = table.readByte();
                    table.readShort();
                    long rightPointer = table.readInt();
                    ArrayList<Short> cellPointers = new ArrayList();
                    for (int i = 0; i < cells; i++) {
                        cellPointers.add(Short.valueOf(table.readShort()));
                    }
                    boolean nextPage = true;
                    while (nextPage) {
                        for (int i = 0; i < cellPointers.size(); i++) {
                            table.seek(((Short) cellPointers.get(i)).shortValue());

                            for (Column column : columns) {
                                if (column.getDataType().equals("int")) {
                                    String value = "" + table.readInt();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        return true;
                                    }

                                } else if (column.getDataType().equals("tinyint")) {
                                    String value = "" + table.readByte();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        return true;
                                    }

                                } else if (column.getDataType().equals("smallint")) {
                                    String value = "" + table.readShort();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        return true;
                                    }

                                } else if (column.getDataType().equals("bigint")) {
                                    String value = "" + table.readLong();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        return true;
                                    }

                                } else if (column.getDataType().equals("real")) {
                                    String value = "" + table.readFloat();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        return true;
                                    }

                                } else if (column.getDataType().equals("double")) {
                                    String value = "" + table.readDouble();
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        return true;
                                    }

                                } else if (column.getDataType().equals("date")) {
                                    String value = utility.convertDateToString(table.readLong());
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        return true;
                                    }

                                } else if (column.getDataType().equals("datetime")) {
                                    String value = utility.convertDateTimeToString(table.readLong());
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        return true;
                                    }
                                } else {
                                    int length = table.readByte();
                                    byte[] bytes = new byte[length];
                                    table.read(bytes, 0, bytes.length);
                                    String value = " " + new String(bytes);
                                    if (column.getColumnName().equals(filterArray[0])) {
                                        if (!value.equals(filterArray[1]))
                                            break;
                                        return true;
                                    }
                                }
                            }
                        }

                        if (rightPointer != 0L) {
                            table.seek(rightPointer);
                            table.readByte();
                            cells = table.readByte();
                            table.readShort();
                            rightPointer = table.readInt();
                            cellPointers = new ArrayList<Short>();
                            for (int i = 0; i < cells; i++) {
                                cellPointers.add(Short.valueOf(table.readShort()));
                            }
                        } else {
                            nextPage = false;
                        }
                    }
                }
                table.close();
            }
        } catch (Exception e) {
            System.out.println("Error, While fectching records from table");
        }

        return false;
    }
}
