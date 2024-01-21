## Measurements

The system was run on the designated server, using the [Griewank function](https://www.sfu.ca/~ssurjano/griewank.html), with 4,8 and 16 nodes; with the following parameters:

```json
{
  "data": [
    [-600, 600, 0.2, 5],
    [-600, 600, 0.2, 5],
    [-600, 600, 0.2, 5]
  ],
  "agg": "MIN",
  "maxItemsPerBatch": 10800000
}
```

### Average Summary

| Measurement         | 4 Nodes             | 8 Nodes             | 16 Nodes            |
|---------------------|---------------------|---------------------|---------------------|
| Worker Throughput   | 0.64 Results/Second | 0.63 Results/Second | 0.6 Results/Second  |
| Combined Throughput | 2.57 Results/Second | 4.85 Results/Second | 9.42 Results/Second |
| Work-time Variation | 1.57%               | 1.95%               | 1.83%               |
| Memory Usage        | 370 MB/Worker       | 367 MB/Worker       | 360 MB/Worker       |
| CPU Usage           | 100%/Worker         | 100%/Worker         | 100%/Worker         |
| Completion Time     | 155 Minutes         | 82.2Minutes         | 42.1Minutes         |
