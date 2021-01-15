package org.ruan4261.eiuc;

import org.apache.poi.ss.usermodel.*;

import java.util.Iterator;

public class Worksheet {

    public static Worksheet openWorkSheet(Workbook workbook, int idx) {
        Worksheet the = new Worksheet();
        the.sheet = workbook.getSheetAt(idx);
        the.workbook = the.sheet.getWorkbook();
        the.drawing = the.sheet.createDrawingPatriarch();
        return the;
    }

    public static Worksheet openWorkSheet(Workbook workbook, String sheetName) {
        Worksheet the = new Worksheet();
        the.sheet = workbook.getSheet(sheetName);
        the.workbook = the.sheet.getWorkbook();
        the.drawing = the.sheet.createDrawingPatriarch();
        return the;
    }

    private Worksheet() {
    }

    /* Instance scope */

    private Workbook workbook;
    private Sheet sheet;
    private Drawing<?> drawing;

    /**
     * @param col             从0开始的列下标, 如果该列不存在会npe, 正常情况下列中的单元格将被迭代操作
     * @param ignoreException 是否抛出异常, 如果否, 本方法不会抛出任何异常, 包括列未找到的npe
     */
    public void operaColumn(CellOperation operation, int col, boolean ignoreException) {
        Iterator<Row> it = sheet.rowIterator();
        while (it.hasNext()) {
            Row row = it.next();
            try {
                Cell cell = row.getCell(col);
                operation.operate(cell);
            } catch (RuntimeException e) {
                e.printStackTrace();
                if (!ignoreException)
                    throw e;
            }
        }
    }

    /**
     * 单元格处理办法
     */
    @FunctionalInterface
    public interface CellOperation {

        void operate(Cell cell);

    }

    public void insertImage(byte[] data, int row, int col) {
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col, row, col + 1, row + 1);// dx1 dy1 dx2 dy2 c1 r1 c2 r2
        System.out.println("Picture anchor: " + anchor.toString());

        drawing.createPicture(anchor, workbook.addPicture(data, Workbook.PICTURE_TYPE_JPEG));
        System.out.println("Picture ok.");
    }
}
