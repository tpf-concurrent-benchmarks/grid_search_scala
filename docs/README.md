# Benchmarks

## Measurements

| Measurement         | 4 Nodes             | 8 Nodes             | 16 Nodes            |
| ------------------- | ------------------- | ------------------- | ------------------- |
| Worker Throughput   | 0.93 Results/Second | 0.97 Results/Second | 0.94 Results/Second |
| Combined Throughput | 3.72 Results/Second | 7.76 Results/Second | 15.1 Results/Second |
| Work-time Variation | 0.7%                | 0.45%               | 1.3%                |
| Memory Usage        | 350 MB/Worker       | 370 MB/Worker       | 365 MB/Worker       |
| CPU Usage           | 99.6%/Worker        | 99.9%/Worker        | 99.9%/Worker        |
| Completion Time     | 102.3 Minutes       | 51.4 Minutes        | 26.6 Minutes        |

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
