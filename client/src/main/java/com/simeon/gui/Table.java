package com.simeon.gui;

import com.simeon.Response;
import com.simeon.ResponseHandler;
import com.simeon.Role;
import com.simeon.UserInfo;
import com.simeon.element.Address;
import com.simeon.element.Coordinates;
import com.simeon.element.Organization;
import com.simeon.element.OrganizationType;
import lombok.NonNull;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.List;

public class Table extends JPanel {
    public static final ResourceBundle lang = ResourceBundle.getBundle("lang");

    public static class CoordinatesRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Coordinates coord = (Coordinates) value;
            JLabel label = new JLabel();
            label.setText(String.format("(%d; %d)", coord.getX(), coord.getY()));
            return label;
        }
    }

    public static class PostalAddressRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (value != null) {
                Address address = (Address) value;
                return new JLabel(address.getZipCode());
            }
            else {
                return null;
            }
        }
    }

    public static class UserInfoRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            UserInfo user = (UserInfo) value;
            return new JLabel(user.getUsername());
        }
    }

    public static class LocalDateRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            LocalDate localDate = (LocalDate) value;
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
            Date date = java.sql.Date.valueOf(localDate);
            return new JLabel(dateFormat.format(date));
        }
    }

    public static class Sorter extends TableRowSorter<TableModel> {
        @Override
        public Comparator<?> getComparator(int columnName) {
            return switch (columnName) {
                default -> new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return (o1).compareTo(o2);
                    }
                };
            };
        }
    }
    public static class TableModel extends AbstractTableModel {
        private final Vector<Organization> collection = new Vector<>();
        private final static String[] header =
                new String[]{"id", "name", "type", "annualTurnover", "coordinates", "postalAddress", "creationDate", "user"};

        public Organization getById(long id) {
            return collection.stream().filter(e -> e.getId() == id).findFirst().get();
        }
        @Override
        public int getRowCount() {
            return collection.size();
        }

        @Override
        public int getColumnCount() {
            return header.length;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0 -> Integer.class;
                case 3 -> Double.class;
                case 4 -> Coordinates.class;
                case 5 -> Address.class;
                case 6 -> LocalDate.class;
                case 7 -> UserInfo.class;
                default -> String.class;
            };
        }

        @Override
        public String getColumnName(int columnIndex) {
            return lang.getString(header[columnIndex]);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Organization entity = collection.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> entity.getId();
                case 1 -> entity.getName();
                case 2 -> entity.getType().toString();
                case 3 -> entity.getAnnualTurnover();
                case 4 -> entity.getCoordinates();
                case 5 -> entity.getPostalAddress();
                case 6 -> entity.getCreationDate();
                default -> entity.getUserInfo();
            };
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public void insertRow(Organization entity) {
            collection.add(entity);
        }
    }

    private JTable table;
    private TableModel tableModel;

    public Table() {
        createGUI();
    }

    private void createGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        tableModel = new TableModel();
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().resizeAndRepaint();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setDefaultRenderer(Coordinates.class, new CoordinatesRender());
        table.setDefaultRenderer(Address.class, new PostalAddressRender());
        table.setDefaultRenderer(UserInfo.class, new UserInfoRender());
        table.setDefaultRenderer(LocalDate.class, new LocalDateRender());

//        table.setRowSorter(new Sorter());
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    public void add(Organization entity) {
        tableModel.insertRow(entity);
        ColumnsAutoSizer.sizeColumnsToFit(table);
    }

    public void setCollection(List<Organization> collection) {
        for (Organization organization : collection) {
            add(organization);
        }
    }

    public Organization getById(long id) {
        return tableModel.getById(id);
    }

    public static void main(String[] args) {
        Organization mock1 = Organization.builder().name("qwerty")
                .type(OrganizationType.COMMERCIAL)
                .annualTurnover(12345.10)
                .postalAddress(new Address("9rjlekf"))
                .userInfo(new UserInfo(1, "simeon", Role.ADMIN))
                .coordinates(new Coordinates(100, 1000))
                .creationDate(LocalDate.now())
                .build();
        Organization mock2 = Organization.builder().name("asdfb").id(1)
                .type(OrganizationType.PUBLIC)
                .annualTurnover(0.124)
                .postalAddress(null)
                .userInfo(new UserInfo(1, "mth", Role.ADMIN))
                .coordinates(new Coordinates(12, 34))
                .creationDate(LocalDate.now().minusDays(1))
                .build();
        Organization mock3 = Organization.builder().name("asdfb").id(1)
                .type(OrganizationType.PUBLIC)
                .annualTurnover(0.124)
                .postalAddress(new Address("qwertt"))
                .userInfo(new UserInfo(1, "mth", Role.ADMIN))
                .coordinates(new Coordinates(12, 34))
                .creationDate(LocalDate.now().minusDays(1))
                .build();

        JFrame main = new JFrame();
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        main.getContentPane().setLayout(new GridLayout(1, 1));
        Table table = new Table();
        table.add(mock1);
        table.add(mock3);
        table.add(mock2);
        main.add(table);
        main.pack();

        main.setVisible(true);
    }
}


class ColumnsAutoSizer {

    public static void sizeColumnsToFit(JTable table) {
        sizeColumnsToFit(table, 5);
    }

    public static void sizeColumnsToFit(JTable table, int columnMargin) {
        JTableHeader tableHeader = table.getTableHeader();

        if(tableHeader == null) {
            // can't auto size a table without a header
            return;
        }

        FontMetrics headerFontMetrics = tableHeader.getFontMetrics(tableHeader.getFont());

        int[] minWidths = new int[table.getColumnCount()];
        int[] maxWidths = new int[table.getColumnCount()];

        for(int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
            int headerWidth = headerFontMetrics.stringWidth(table.getColumnName(columnIndex));

            minWidths[columnIndex] = headerWidth + columnMargin;

            int maxWidth = getMaximalRequiredColumnWidth(table, columnIndex, headerWidth);

            maxWidths[columnIndex] = Math.max(maxWidth, minWidths[columnIndex]) + columnMargin;
        }

        adjustMaximumWidths(table, minWidths, maxWidths);

        for(int i = 0; i < minWidths.length; i++) {
            if(minWidths[i] > 0) {
                table.getColumnModel().getColumn(i).setMinWidth(minWidths[i]);
            }

            if(maxWidths[i] > 0) {
                table.getColumnModel().getColumn(i).setMaxWidth(maxWidths[i]);

                table.getColumnModel().getColumn(i).setWidth(maxWidths[i]);
            }
        }
    }

    private static void adjustMaximumWidths(JTable table, int[] minWidths, int[] maxWidths) {
        if(table.getWidth() > 0) {
            // to prevent infinite loops in exceptional situations
            int breaker = 0;

            // keep stealing one pixel of the maximum width of the highest column until we can fit in the width of the table
            while(sum(maxWidths) > table.getWidth() && breaker < 10000) {
                int highestWidthIndex = findLargestIndex(maxWidths);

                maxWidths[highestWidthIndex] -= 1;

                maxWidths[highestWidthIndex] = Math.max(maxWidths[highestWidthIndex], minWidths[highestWidthIndex]);

                breaker++;
            }
        }
    }

    private static int getMaximalRequiredColumnWidth(JTable table, int columnIndex, int headerWidth) {
        int maxWidth = headerWidth;

        TableColumn column = table.getColumnModel().getColumn(columnIndex);

        TableCellRenderer cellRenderer = column.getCellRenderer();

        if(cellRenderer == null) {
            cellRenderer = new DefaultTableCellRenderer();
        }

        for(int row = 0; row < table.getModel().getRowCount(); row++) {
            Component rendererComponent = cellRenderer.getTableCellRendererComponent(table,
                    table.getModel().getValueAt(row, columnIndex),
                    false,
                    false,
                    row,
                    columnIndex);

            double valueWidth = rendererComponent.getPreferredSize().getWidth();

            maxWidth = (int) Math.max(maxWidth, valueWidth);
        }

        return maxWidth;
    }

    private static int findLargestIndex(int[] widths) {
        int largestIndex = 0;
        int largestValue = 0;

        for(int i = 0; i < widths.length; i++) {
            if(widths[i] > largestValue) {
                largestIndex = i;
                largestValue = widths[i];
            }
        }

        return largestIndex;
    }

    private static int sum(int[] widths) {
        int sum = 0;

        for(int width : widths) {
            sum += width;
        }

        return sum;
    }

}