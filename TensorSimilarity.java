import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map;

// <dependency>
//     <groupId>com.google.code.gson</groupId>
//     <artifactId>gson</artifactId>
//     <version>2.8.6</version>
// </dependency>

public class TensorSimilarity {

    static class BookData {
        String isbn;
        List<Double> tensor;

        public double calculateCosineSimilarity(List<Double> otherTensor) {
            double dotProduct = 0.0;
            double normA = 0.0;
            double normB = 0.0;
            for (int i = 0; i < tensor.size(); i++) {
                dotProduct += tensor.get(i) * otherTensor.get(i);
                normA += Math.pow(tensor.get(i), 2);
                normB += Math.pow(otherTensor.get(i), 2);
            }
            return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        }
    }

    public static List<Double> calculateAverage(List<List<Double>> tensors) {
        // 初始化一个列表来保存平均值结果，大小为825，所有值初始化为0.0
        List<Double> averageTensor = new ArrayList<>();
        for (int i = 0; i < 825; i++) {
            averageTensor.add(0.0);
        }

        // 计算所有tensor的和
        for (List<Double> tensor : tensors) {
            for (int i = 0; i < tensor.size(); i++) {
                double sum = averageTensor.get(i) + tensor.get(i);
                averageTensor.set(i, sum);
            }
        }

        // 计算平均值
        int totalTensors = tensors.size();
        for (int i = 0; i < averageTensor.size(); i++) {
            double avg = averageTensor.get(i) / totalTensors;
            averageTensor.set(i, avg);
        }

        return averageTensor;
    }

    public static void main(String[] args) {
        // 假设有多个825维度的tensor
        List<List<Double>> tensors = new ArrayList<>();
        tensors.add(generateRandomTensor());
        tensors.add(generateRandomTensor());
        tensors.add(generateRandomTensor());

        // 计算平均tensor
        List<Double> averageTensor = calculateAverage(tensors);

        // 输出平均tensor
        System.out.println("Average Tensor: " + averageTensor);


        Gson gson = new Gson();
        try {
            Type bookListType = new TypeToken<ArrayList<BookData>>(){}.getType();
            List<BookData> books = gson.fromJson(new FileReader("path/to/your/tensor_data.json"), bookListType);

            // 示例：测试用tensor
            List<Double> testTensor = new ArrayList<>();
            // 填充testTensor数据
            for (int i = 0; i < 825; i++) {
                testTensor.add(Math.random());  // 使用随机数据填充
            }

            // 使用优先队列找到最高的10个相似度
            PriorityQueue<BookData> pq = new PriorityQueue<>(10, (a, b) -> Double.compare(b.calculateCosineSimilarity(testTensor), a.calculateCosineSimilarity(testTensor)));
            for (BookData book : books) {
                pq.offer(book);
                if (pq.size() > 10) {
                    pq.poll();
                }
            }

            // 输出最相似的前10本书
            while (!pq.isEmpty()) {
                BookData book = pq.poll();
                System.out.println("ISBN: " + book.isbn + " - Similarity: " + book.calculateCosineSimilarity(testTensor));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
