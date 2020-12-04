import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        int[][] matrix = new int[9][9];//存放数独数据的数组
        resetIntArray(matrix);
        try {
            readSaveFile("autosave",matrix);
            System.out.println("读取了上次的自动存档");
        } catch (FileNotFoundException e) {
            System.out.println("暂无自动存档");
        }
        while (true) {
            System.out.println("请输入数字开启模式：1.按顺序录入数组；2.进入编辑模式；");
            System.out.println("3.显示并检验数独正确性；4.重置九宫格（清零）；5.写入存档；");
            System.out.println("6.读取存档；7.结束程序");
            int mode = input.nextInt();
            if (mode == 7) {
                autoSave(matrix);
                System.out.println("你的数独九宫格如下：");
                displayMatrix(matrix);
                System.exit(1);
            } else if (mode==6){
                System.out.println("输入存档名：");
                String filename = input.next();
                try {
                    readSaveFile(filename, matrix);
                    autoSave(matrix);
                    System.out.println("读取了存档"+filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("读取存档出错");
                }
            } else if (mode == 5) {
                System.out.println("输入存档名：");
                String filename = input.next();
                try {
                    writeInSaveFile(filename, matrix);
                    System.out.println("写入了存档"+filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("写入存档出错");
                }
            } else if (mode == 4) {
                resetIntArray(matrix);
                autoSave(matrix);
                System.out.println("九宫格已重置！");
                displayMatrix(matrix);
            } else if (mode == 3) {
                displayMatrix(matrix);
                sudokuExamine(matrix);
            } else if (mode == 2) {
                System.out.println("你进入了编辑模式，请以以下格式输入：");
                System.out.println("示例：419");
                System.out.println("其中，4是行数，1是列数，9是该坐标(4,1)中要填的数字，如果Num为0则表示空置。");
                System.out.println("如果输入'000'，则是退出编辑模式。");
                while (true) {
                    int editOrder = input.nextInt();
                    if (editOrder == 0) {
                        autoSave(matrix);
                        System.out.println("退出编辑模式。");
                        break;
                    } else if (editOrder < 110 || editOrder > 999 || editOrder % 100 < 10) {
                        System.out.println("检测到了错误的输入，请再试一次");
                        //不允许行或列=0，且必须是3位数，否则打回
                    } else {
                        int row = editOrder / 100;//第一位是行
                        int column = (editOrder % 100) / 10;//第二位是列
                        int number = editOrder % 10;//第三位是填入的数字
                        if (number != 0) {
                            System.out.println("在" + row + "行" + column + "列填入了数字" + number);
                        } else {
                            System.out.println("将" + row + "行" + column + "列的数字清除了");
                        }
                        matrix[row - 1][column - 1] = number;
                    }
                }
            } else if (mode == 1) {
                System.out.println("请输入81个0-9的数字，用空格或回车隔开。");
                for (int row = 0; row < 9; row++) {
                    for (int column = 0; column < 9; column++) {
                        while (true) {
                            int number = input.nextInt();
                            if (number >= 0 && number <= 9) {
                                matrix[row][column] = number;
                                break;
                            } else {
                                System.out.println("检测到了错误的输入，请再试一次");
                            }
                        }
                    }
                }
                autoSave(matrix);
                System.out.println("录入结束");
            } else {
                System.out.println("检测到了错误的输入，什么也没发生。");
            }
        }
    }
    //函数声明

    static void resetIntArray(int[] array) {
        java.util.Arrays.fill(array, 0);
    }

    static void resetIntArray(int[][] array) {
        for (int[] row : array) {
            java.util.Arrays.fill(row, 0);
        }
    }

    static void displayMatrix(int[][] matrix) {
        for (int row = 1; row <= 19; row++) {
            if (row == 1) {
                System.out.println("╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗");
            } else if (row == 19) {
                System.out.println("╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝");
            } else if (row == 7 || row == 13) {
                System.out.println("╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣");
            } else if (row % 2 == 0) {
                for (int column = 1; column <= 37; column++) {
                    if (column % 12 == 1) {
                        System.out.print("║");
                    } else if (column % 4 == 1) {
                        System.out.print("|");
                    } else if (column % 4 == 3) {
                        int temp = matrix[row / 2 - 1][column / 4];
                        if (temp != 0) {
                            System.out.print(temp);
                        } else {
                            System.out.print(" ");
                        }
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.print("\n");
            } else {
                System.out.println("╟---+---+---╫---+---+---╫---+---+---╢");
            }
        }
    }

    static void sudokuExamine(int[][] matrix) {
        int[] numberCount = new int[10];
        //临时数组，第0位表示空缺数，1到9分别对应的是该数字的出现数
        //行检查
        for (int row = 0; row < 9; row++) {
            resetIntArray(numberCount);
            for (int column = 0; column < 9; column++) {
                int number = matrix[row][column];
                numberCount[number]++;
            }//计数
            for (int num = 1; num <= 9; num++) {
                if (numberCount[num] >= 2) {
                    System.out.println("行检查：第" + (row + 1) + "行中，数字" + num + "存在重复");
                }
            }//报错
        }
        //列检查
        for (int column = 0; column < 9; column++) {
            resetIntArray(numberCount);
            for (int row = 0; row < 9; row++) {
                int number = matrix[row][column];
                numberCount[number]++;
            }//计数
            for (int num = 1; num <= 9; num++) {
                if (numberCount[num] >= 2) {
                    System.out.println("列检查：第" + (column + 1) + "列中，数字" + num + "存在重复");
                }
            }//报错
        }
        //宫检查
        for (int block = 0; block < 9; block++) {
            resetIntArray(numberCount);
            int startRow = (block / 3) * 3;
            int startColumn = (block % 3) * 3;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int number = matrix[startRow + i][startColumn + j];
                    numberCount[number]++;
                }
            }//计数
            for (int num = 1; num <= 9; num++) {
                if (numberCount[num] >= 2) {
                    System.out.println("宫检查：第" + (block + 1) + "宫中，数字" + num + "存在重复");
                }
            }//报错
        }
    }

    static void writeInSaveFile(String filename, int[][] data) throws FileNotFoundException {
        File path = new File("saves");
        if (!path.exists()){
            path.mkdirs();
        }//如果没有saves文件夹，就新建一个
        File file = new File("saves/" + filename + ".txt");
        try (PrintWriter output = new PrintWriter(file)) {
            for (int row = 0; row < 9; row++) {
                for (int column = 0; column < 9; column++) {
                    output.print(data[row][column]);
                    if (column != 8) {
                        output.print(" ");
                    } else {
                        output.print("\n");
                    }
                }
            }
        }
    }

    static void readSaveFile(String filename, int[][] outputData) throws FileNotFoundException {
        File file = new File("saves/" + filename + ".txt");
        try (Scanner fileInput = new Scanner(file)) {
            for (int row = 0; row < 9; row++) {
                for (int column = 0; column < 9; column++) {
                    if (fileInput.hasNext()) {
                        outputData[row][column] = fileInput.nextInt();
                    }
                }
            }
        }
    }

    static void autoSave(int[][] data) throws FileNotFoundException {
        writeInSaveFile("autosave",data);
    }
}

