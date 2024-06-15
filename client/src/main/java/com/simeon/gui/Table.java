package com.simeon.gui;

import com.simeon.UserInfo;
import com.simeon.element.Address;
import com.simeon.element.Coordinates;
import com.simeon.element.Organization;
import com.simeon.element.OrganizationType;
import com.simeon.element.comparator.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

public class Table extends JPanel {
    public static ResourceBundle lang = ResourceBundle.getBundle("lang");

    public static class CoordinatesRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Coordinates coord = (Coordinates) value;
            setText(String.format("(%d; %d)", coord.getX(), coord.getY()));
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            setAlignmentX(CENTER_ALIGNMENT);
            return this;
        }
    }

    public static class PostalAddressRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            if (value != null) {
                Address address = (Address) value;
                setText(address.getZipCode());
            }
            return this;
        }
    }

    public static class UserInfoRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            UserInfo user = (UserInfo) value;
            setText(user.getUsername());
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }

    public static class LocalDateRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            LocalDate localDate = (LocalDate) value;
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
            Date date = java.sql.Date.valueOf(localDate);
            setText(dateFormat.format(date));
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }

    public static class Sorter extends TableRowSorter<TableModel> {
        @Override
        public Comparator<?> getComparator(int columnName) {
            return switch (columnName) {
                case 0 -> new IDComparator();
                case 1 -> new NameComparator();
                case 2 -> new TypeComparator();
                case 3 -> new AnnualTurnoverComparator();
                case 4 -> new CoordinateComparator();
                case 5 -> new PostalAddressComparator();
                case 6 -> new CreationDateComparator();
                default -> new UserComparator();
            };
        }
    }
    public static class TableModel extends AbstractTableModel {
        private final ReentrantLock lock = new ReentrantLock();
        private final Vector<Organization> collection = new Vector<>();

        private Predicate<Organization> filter = organization -> true;
        private final static String[] header =
                new String[]{"id", "name", "type", "annualTurnover", "coordinates", "postalAddress", "creationDate", "user"};

        public Organization getById(long id) {
            lock.lock();
            try {
                return collection.stream().filter(e -> e.getId() == id).findFirst().get();
            }
            finally {
                lock.unlock();
            }
        }
        @Override
        public int getRowCount() {
            lock.lock();
            try {
                return collection.stream().filter(filter).toList().size();
            }
            finally {
                lock.unlock();
            }
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
            lock.lock();
            try {
                List<Organization> filt_list = collection.stream().filter(filter).toList();
                Organization entity = filt_list.get(rowIndex);
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
            finally {
                lock.unlock();
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public void insertRow(Organization entity) {
            lock.lock();
            try {
                Optional<Organization> op = collection.stream().filter(e -> e.getId() == entity.getId()).findFirst();
                int index = -1;
                if (op.isPresent()) {
                     index = collection.indexOf(op.get());
                }
                if (index > -1) {
                    collection.set(index, entity);
                }
                else {
                    collection.add(entity);
                }
//                collection.removeIf(e -> e.getId() == entity.getId());
//                collection.add(entity);
                fireTableDataChanged();
            }
            finally {
                lock.unlock();
            }
        }

        public List<Organization> getCollection() {
            lock.lock();
            try {
                return new ArrayList<>(collection);
            }
            finally {
                lock.unlock();
            }
        }

        public void removeRow(Organization entity) {
            lock.lock();
            try {
                collection.removeIf((e) -> e.getId() == entity.getId());
                fireTableDataChanged();
            }
            finally {
                lock.unlock();
            }
        }

        public void setFiler(String type) {
            OrganizationType organizationType = OrganizationType.getByName(type);
            if (organizationType != null) {
                filter = organization -> organization.getType().compareTo(organizationType) >= 0;
            }
            else {
                filter = (e) -> true;
            }

            fireTableDataChanged();
        }
    }

    private JTable table;
    private TableModel tableModel;
    private final GUI gui;
    public Table(GUI gui) {
        this.gui = gui;
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

        table.setRowSorter(new Sorter());
        table.setAutoCreateRowSorter(true);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    gui.update((Long) tableModel.getValueAt(row, 0));
                }
                else if (e.getClickCount() == 1 && table.getSelectedRow() != -1) {
                    long id = (long) tableModel.getValueAt(table.getSelectedRow(), 0);
                    gui.select(id);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    public void add(Organization entity) {
        tableModel.insertRow(entity);
        ColumnsAutoSizer.sizeColumnsToFit(table);
    }

    public void delete(Organization entity) {
        tableModel.removeRow(entity);
        ColumnsAutoSizer.sizeColumnsToFit(table);
        table.repaint();
        table.revalidate();
    }

    public Organization getById(long id) {
        return tableModel.getById(id);
    }

    public List<Organization> getCollection() {
        return tableModel.getCollection();
    }

    public void setFilterType(String type) {
        tableModel.setFiler(type);
        table.repaint();
        table.revalidate();
    }

    public void relocale() {
        lang = ResourceBundle.getBundle("lang");
        String[] header =
                new String[]{"id", "name", "type", "annualTurnover", "coordinates", "postalAddress", "creationDate", "user"};
        for (int i = 0; i < header.length; ++i) {
            TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
            column.setHeaderValue(lang.getString(header[i]));
        }
        table.getTableHeader().resizeAndRepaint();
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