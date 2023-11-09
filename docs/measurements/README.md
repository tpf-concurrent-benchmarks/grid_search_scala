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
| Worker Throughput   | 7.3 Results/Minute  | 7.25 Results/Minute | 7.00 Results/Minute |
| Combined Throughput | 29.2 Results/Minute | 58.0 Results/Minute | 112 Results/Minute  |
| Work-time Variation | 0.9%                | 1%                  | 3%                  |
| Memory Usage        | 380 MB/Worker       | 370 MB/Worker       | 365 MB/Worker       |
| CPU Usage           | 99.9%/Worker (?)    | 99.9%/Worker (?)    | 99.9%/Worker (?)    |
| Completion Time     | 100 Minutes (?)     | 50 Minutes          | 25 Minutes          |
