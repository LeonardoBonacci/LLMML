package guru.bonacci.llmml;

import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Map;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

public class TransactionValidator {

    private final OrtEnvironment env;
    private final OrtSession session;
    private final String inputName;

    public TransactionValidator(String modelPath) throws OrtException {
        this.env = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
        this.session = env.createSession(modelPath, opts);

        // Same as: session.get_inputs()[0].name
        this.inputName = session.getInputNames().iterator().next();
    }

    /**
     * tx: amount, country (0/1), merchant (0/1)
     */
    public boolean isValidTransaction(float amount, float country, float merchant)
            throws OrtException {

        // Prepare input tensor: shape [1, 3]
        float[] inputData = new float[] { amount, country, merchant };
        long[] shape = new long[] { 1, 3 };

        try (OnnxTensor inputTensor =
                     OnnxTensor.createTensor(env, FloatBuffer.wrap(inputData), shape);
             OrtSession.Result result =
                     session.run(Collections.singletonMap(inputName, inputTensor))) {

            Object value = result.get(0).getValue();

            float prob;

            // Handle common ONNX output shapes safely
            if (value instanceof float[][][]) {
                prob = ((float[][][]) value)[0][0][0];
            } else if (value instanceof float[][]) {
                prob = ((float[][]) value)[0][0];
            } else if (value instanceof float[]) {
                prob = ((float[]) value)[0];
            } else {
                throw new IllegalStateException("Unexpected output type: " + value.getClass());
            }

            return prob > 0.5f;
        }
    }
    
    public float predictProbability(float amount, float country, float merchant)
        throws OrtException {

    float[] inputData = new float[] { amount, country, merchant };
    long[] shape = new long[] { 1, 3 };

    try (OnnxTensor inputTensor =
                 OnnxTensor.createTensor(env, FloatBuffer.wrap(inputData), shape);
         OrtSession.Result result =
                 session.run(Map.of(inputName, inputTensor))) {

        Object value = result.get(0).getValue();

        if (value instanceof float[][][]) {
            return ((float[][][]) value)[0][0][0];
        } else if (value instanceof float[][]) {
            return ((float[][]) value)[0][0];
        } else {
            return ((float[]) value)[0];
        }
    }
}

    public void close() throws OrtException {
        session.close();
        env.close();
    }
}