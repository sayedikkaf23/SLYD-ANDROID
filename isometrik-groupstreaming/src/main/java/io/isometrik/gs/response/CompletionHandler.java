package io.isometrik.gs.response;

import io.isometrik.gs.response.error.IsometrikError;

/**
 * The interface Completion handler to handle the response of the remote query.
 *
 * @param <T> the type parameter
 */
public interface CompletionHandler<T> {
  /**
   * On complete.
   *
   * @param var1 the var 1, instance of the result type
   * @param var2 the var 2, instance of the isometrik error
   * @see io.isometrik.gs.response.error.IsometrikError
   */
  void onComplete(T var1, IsometrikError var2);
}


