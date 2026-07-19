package br.com.wagnersoft.babilonia.exception;

/**
 *{@code BabiloniaException} é a superclasse das exceções que podem ser lançadas pela API
 *durante sua execução normal.
 *  
 *<p>Esta exceção está associada ao status http 400 (requisição inválida) e suas subsclasses aos
 *status http subsequentes da faixa 400.</p>
 *
 * @author Wagner Lopes
 * @since 1.0
 * @version 1.0
 */
public class BabiloniaException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public BabiloniaException(String mensagem) {
    super(mensagem);
  }

  public BabiloniaException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }

}
