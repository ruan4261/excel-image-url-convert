package org.ruan4261.eiuc;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ruan4261.eiuc.strategy.ConstructWithFileIdStrategy;
import org.ruan4261.eiuc.util.HttpUtils;

import java.io.*;

/**
 * Excel表中有一列保存信息为图片链接(以html中的a标签形式存在, 需要获取到内部的href属性)
 * 将链接转换为真实图片
 */
public class Launcher {

    final static String PATH = "/Users/a4261/Downloads/导出通行费.xlsx";
    final static String COOKIE = "";
    final static int TARGET_COLUMN = 11;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(PATH);
        InputStream input = new FileInputStream(file);
        try (XSSFWorkbook workbook = new XSSFWorkbook(input)) {
            // 关闭输入流, 否则会和之后的输出流冲突
            input.close();
            Worksheet sheet = Worksheet.openWorkSheet(workbook, "导出工作表");

            // 目标列每个单元格迭代
            sheet.operaColumn(cell -> {
                int row = cell.getRowIndex();
                int col = cell.getColumnIndex();
                System.out.println("--------------------- Cell{row: " + row + ", col: " + col + "} Opera ---------------------");
                String content = cell.getStringCellValue();
                // 图片链接
                String url = new ConstructWithFileIdStrategy().resolve(content);
                System.out.println(url);

                try {
                    // 清空原单元格内信息
                    cell.setCellValue((String) null);
                    byte[] data = HttpUtils.getByteData(url, COOKIE);
                    sheet.insertImage(data, row, col);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("--------------------- Cell{row: " + row + ", col: " + col + "} OK ---------------------");
            }, TARGET_COLUMN, true);

            // 记得输出, 否则修改无效
            try (OutputStream output = new FileOutputStream(file)) {
                workbook.write(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
