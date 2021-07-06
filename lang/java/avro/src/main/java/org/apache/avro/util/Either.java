package org.apache.avro.util;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<L, R> {
  public static <L, R> Either<L, R> ofLeft(L value) {
    return new Left<>(value);
  }

  public static <L, R> Either<L, R> ofRight(R value) {
    return new Right<>(value);
  }

  public abstract L getLeft();

  public abstract R getRight();

  public abstract boolean isLeft();

  public abstract boolean isRight();

  public abstract void ifLeft(Consumer<? super L> consumer);

  public abstract <L2> Either<L2, R> map(Function<? super L, ? extends L2> f);

  private static class Left<L, R> extends Either<L, R> {
    private final L value;

    Left(L value) {
      this.value = value;
    }

    @Override
    public L getLeft() {
      return value;
    }

    @Override
    public R getRight() {
      throw new IllegalStateException("Calling getRight on a left value: " + this);
    }

    @Override
    public boolean isLeft() {
      return true;
    }

    @Override
    public boolean isRight() {
      return false;
    }

    @Override
    public void ifLeft(Consumer<? super L> consumer) {
      consumer.accept(value);
    }

    @Override
    public <L2> Either<L2, R> map(Function<? super L, ? extends L2> f) {
      return Either.ofLeft(f.apply(value));
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      Left<?, ?> left = (Left<?, ?>) o;
      return Objects.equals(value, left.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    @Override
    public String toString() {
      return "Left{" + value + '}';
    }
  }

  private static class Right<L, R> extends Either<L, R> {
    private final R value;

    Right(R value) {
      this.value = value;
    }

    @Override
    public L getLeft() {
      throw new IllegalStateException("Calling getLeft on a right value: " + this);
    }

    @Override
    public R getRight() {
      return value;
    }

    @Override
    public boolean isLeft() {
      return false;
    }

    @Override
    public boolean isRight() {
      return true;
    }

    @Override
    public void ifLeft(Consumer<? super L> consumer) {
    }

    @Override
    public <L2> Either<L2, R> map(Function<? super L, ? extends L2> f) {
      return (Either<L2, R>) this;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o)
        return true;
      if (o == null || getClass() != o.getClass())
        return false;
      Right<?, ?> right = (Right<?, ?>) o;
      return Objects.equals(value, right.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    @Override
    public String toString() {
      return "Right{" + value + '}';
    }
  }
}
