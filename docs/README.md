# Benchmarks

## Measurements

| Measurement         | 4 Nodes             | 8 Nodes             | 16 Nodes            |
| ------------------- | ------------------- | ------------------- | ------------------- |
| Worker Throughput   | 7.3 Results/Minute  | 7.25 Results/Minute | 7.00 Results/Minute |
| Combined Throughput | 29.2 Results/Minute | 58.0 Results/Minute | 112 Results/Minute  |
| Work-time Variation | 0.9%                | 1%                  | 3%                  |
| Memory Usage        | 380 MB/Worker       | 370 MB/Worker       | 365 MB/Worker       |
| CPU Usage           | 99.9%/Worker (?)    | 99.9%/Worker (?)    | 99.9%/Worker (?)    |
| Completion Time     | 100 Minutes         | 50 Minutes          | 25 Minutes          |

Average measurements using the [specified configuration](measurements/README.md)

## Subjective analysis

### Ease of development

Scala has a fast learning curve, and is very expressive.
The high level abstractions and garbage collection simplify development greatly.
Futures and promises are a great way to handle asynchronous code.
SBT provides an easy way to manage dependencies and build the project.

### External Resources

Scala is capable of using Java libraries, which opens up a huge amount of resources.
The compatibility with libraries is not always perfect, but it is usually possible to find a workaround.
Scala is commonly used with [Akka](https://akka.io/), an actor model library used for concurrent and distributed systems; we've been unable to use it effectively due to clustering, building and other issues.
