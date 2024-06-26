package funsets

class FunSetSuite extends munit.FunSuite:

  import FunSets.*

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets:
    val singleton1 = singletonSet(1)
    val singleton2 = singletonSet(2)
    val singleton3 = singletonSet(3)
    val containsPositive:FunSet = x => x >= 0
    val containsNegative:FunSet = x => x < 0

    test("singleton set one contains one") {

    new TestSets:
      assert(contains(singleton1, 1), "Singleton")
      assert(contains(singleton2, 2), "Singleton")
      assert(contains(singleton3, 3), "Singleton")
  }

  test("union contains all elements of each set") {
    new TestSets:
      val s = union(singleton1, singleton2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
  }

  test("singleton only contains singleton") {
    new TestSets:
      assert(!contains(singleton1, 2), "Singleton")
      assert(!contains(singleton1, -1), "Singleton")
      assert(!contains(singleton1, 1001), "Singleton")
  }

  test("intersected sets contains common elements") {
    new TestSets:
      assert(contains(intersect(singleton1, containsPositive),1))
      assert(!contains(intersect(singleton1, containsPositive),5))
      assert(!contains(intersect(singleton1, containsPositive),-1))
  }

  test("intersected sets with no intersection contain nothing") {
    new TestSets:
      assert(!exists(intersect(containsNegative,containsPositive), x => true))
  }

  test("mapped sets contain mapped elements") {
    new TestSets:
      assert(contains(map(containsPositive, x => x * 2),2))
      assert(contains(map(containsPositive, x => x * 2),500))
  }

  test("mapped sets only contained mapped elements") {
    new TestSets:
      assert(!contains(map(containsPositive, x => x * 2),1))
      assert(!contains(map(containsPositive, x => x * 2),501))
  }

  import scala.concurrent.duration.*
  override val munitTimeout = 10.seconds
