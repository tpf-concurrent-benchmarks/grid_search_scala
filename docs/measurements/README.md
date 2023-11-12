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
| ------------------- | ------------------- | ------------------- | ------------------- |
| Worker Throughput   | 0.93 Results/Second | 0.97 Results/Second | 0.94 Results/Second |
| Combined Throughput | 3.72 Results/Second | 7.76 Results/Second | 15.1 Results/Second |
| Work-time Variation | 0.7%                | 0.45%               | 1.3%                |
| Memory Usage        | 350 MB/Worker       | 370 MB/Worker       | 365 MB/Worker       |
| CPU Usage           | 99.6%/Worker        | 99.9%/Worker        | 99.9%/Worker        |
| Completion Time     | 102.3 Minutes       | 51.4 Minutes        | 26.6 Minutes        |
