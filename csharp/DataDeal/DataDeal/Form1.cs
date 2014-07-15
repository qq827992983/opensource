using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace DataDeal
{
    public partial class Form1 : Form
    {
        string file1;
        string file2;
        string file3;

        public Form1()
        {
            file1 = "";
            file2 = "";
            file3 = "";
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            openFileDialog1.ShowDialog();
            file1 = openFileDialog1.FileName;
            textBox1.Text = file1;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            openFileDialog2.ShowDialog();
            file2 = openFileDialog2.FileName;
            textBox2.Text = file2;
        }

        private void button3_Click(object sender, EventArgs e)
        {
            openFileDialog3.ShowDialog();
            file3 = openFileDialog3.FileName;
            textBox3.Text = file3;
        }

        private void button4_Click(object sender, EventArgs e)
        {
            int size = 0;
            if (file1.Length > 0)
                size++;
            if (file2.Length > 0)
                size++;
            if (file3.Length > 0)
                size++;
            switch (size)
            {
                case 0:
                    MessageBox.Show("您未选择任何文件，请选择文件！");
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    MessageBox.Show("程序出现异常，请检查您的操作步骤！");
                    break;
            }
        }

        private void button5_Click(object sender, EventArgs e)
        {
            MessageBox.Show("提示：分别选择三个文件，然后点击\"计算结果\"按钮,会提示结果保存的位置！");
        }
    }
}
