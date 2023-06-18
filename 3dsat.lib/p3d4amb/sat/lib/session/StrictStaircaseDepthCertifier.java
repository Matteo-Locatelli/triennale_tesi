package p3d4amb.sat.lib.session;

import static p3d4amb.sat.lib.session.TestSession.Result.CONTINUE;
import static p3d4amb.sat.lib.session.TestSession.Result.FINISH_CERTIFIED;
import static p3d4amb.sat.lib.session.TestSession.Result.FINISH_NOT_CERTIFIED;

import org.apache.log4j.Logger;

/*! \page certifier Certificazione
*
* La certificazione della stero acuità si basa sul principio che un livello è certificato se una persona riesce ad indovinarne tre 
* senza sbagliarne più di una (un errore è perdonato) per quel livello.
* 
* Quindi l'applicazione inizialmente cerca di proporre immagini sempre più difficili. Inizialmente il target è la disparità minima ammessa dal monitor e la disparità iniziale
* è quella decisa dall'utente. 
*
* In caso di risposta corretta, la depth viene decrementata a meno che sia quella il target o che sia il minimo.
* Dopo tre risposte corrette nel target, la depth viene certificata e si ha finito.
*
* In caso di duplice errore (anche non consecutivo) nel livello corrente, la depth viene incrementata e di cerca di certificare quella (target), 
* a meno che non si raggiunga il massimo (per ora uguale al punto dipartenza).
* 
* 
*/

/**
 * certifies a Depth Requirements: * if the solution is right, the depth is
 * decreased (unless is already 1).
 *
 * @author garganti
 */
public class StrictStaircaseDepthCertifier extends DepthCertBase{

	/** The Constant RIGHT_ANSWERS_TO_CERTIFY. */
	private static final int RIGHT_ANSWERS_TO_CERTIFY = 3;

	/** The Constant WRONG_ANSWERS_TO_STOP_DEPTH. */
	private static final int WRONG_ANSWERS_TO_STOP_DEPTH = 2;

	/**  maxDepth - utilizzata per stabilire il massimo a cui risalire */
	private int maxDepth;

	/** The target depth. */
	// target
	private int targetDepth = 1;

	/** The ansstore. */
	// register all the asnwers for depth
	private AnswersStore ansstore;

	/** The logger. */
	private final Logger logger = Logger.getLogger(StrictStaircaseDepthCertifier.class);

	/**
	 * Instantiates a new depth certifier.
	 *
	 * @param initDepth
	 *            start from
	 * @param maxdepth
	 *            finish maximum at
	 */
	public StrictStaircaseDepthCertifier(int initDepth, int maxdepth) {
		logger.debug("creating certifier with init " + initDepth + " and max depth " + maxdepth);
		certifierStatus = new CertifierStatus();
		if (initDepth <= maxdepth && maxdepth >= 1 && initDepth >= 1)
			certifierStatus.currentDepth = initDepth;
		else
			throw new IllegalArgumentException();
		this.maxDepth = maxdepth;
		ansstore = new AnswersStore(maxdepth);
		certifierStatus.currentResult = TestSession.Result.CONTINUE;
	}

	/**
	 * Gets the current status.
	 *
	 * @return next depth. return -1 if finished
	 */
	@Override
	public CertifierStatus getCurrentStatus() {
		logger.debug("current status " + certifierStatus.currentResult + " (at depth " + certifierStatus.currentDepth
				+ ") target " + targetDepth);
		assert certifierStatus.currentDepth > 0;
		// finished certified -> target reached
		assert !(certifierStatus.currentResult == FINISH_CERTIFIED)
				|| certifierStatus.currentDepth == targetDepth;
		// non certified / CONTINUE -> target not reached ( yet or queal to max)
		assert !(certifierStatus.currentResult == CONTINUE
				|| certifierStatus.currentResult == FINISH_NOT_CERTIFIED)
				|| (certifierStatus.currentDepth >= targetDepth || certifierStatus.currentDepth == maxDepth);
		// se continue, not reached
		assert !(certifierStatus.currentResult == CONTINUE)
				|| (ansstore.getAnswers(targetDepth, DepthCertBase.Solution.RIGHT) < RIGHT_ANSWERS_TO_CERTIFY);
		assert !(certifierStatus.currentResult == CONTINUE)
				|| (ansstore.getAnswers(targetDepth, DepthCertBase.Solution.WRONG) < WRONG_ANSWERS_TO_STOP_DEPTH);

		return certifierStatus;
	}

	/**
	 * Compute the next depth.
	 * 
	 * @param solution
	 *            the solution (WRONG può venire da uno skip) NULL non fare
	 *            nulla
	 */
	@Override
	public
	void computeNextDepth(DepthCertBase.Solution solution) {
		assert solution != null;
		if (solution == DepthCertBase.Solution.NULL) {
			// nothing to do
		} else {
			// register current answer
			ansstore.register(certifierStatus.currentDepth, solution);
			if (solution == DepthCertBase.Solution.RIGHT) {
				// check if going to target
				if (certifierStatus.currentDepth > targetDepth) {
					// still trying to reach target
					assert certifierStatus.currentDepth > 0;
					certifierStatus.currentDepth--;
					certifierStatus.currentResult = CONTINUE;
				} else {
					// in target
					assert certifierStatus.currentDepth == targetDepth;
					// in targetDepth
					if (ansstore.getAnswers(certifierStatus.currentDepth, DepthCertBase.Solution.RIGHT) >= RIGHT_ANSWERS_TO_CERTIFY) {
						certifierStatus.currentResult = FINISH_CERTIFIED;
					} else {
						certifierStatus.currentResult = CONTINUE;
					}

				}
			} else {
				assert (solution == DepthCertBase.Solution.WRONG);
				if (ansstore.getAnswers(certifierStatus.currentDepth, DepthCertBase.Solution.WRONG) >= WRONG_ANSWERS_TO_STOP_DEPTH) {
					if (certifierStatus.currentDepth < maxDepth) {
						// go up one level
						// error and already an error in current depth
						certifierStatus.currentDepth++;
						targetDepth = certifierStatus.currentDepth;
						certifierStatus.currentResult = CONTINUE;
					} else {
						// stop certifying
						certifierStatus.currentResult = FINISH_NOT_CERTIFIED;
					}
				} else {
					certifierStatus.currentResult = CONTINUE;
				}
			}
		}
	}

	/**
	 * Guesses in target.
	 *
	 * @param type
	 *            the type
	 * @return the int
	 */
	// useful for testing
	int guessesInTarget(DepthCertBase.Solution type) {
		return ansstore.getAnswers(targetDepth, type);
	}

	/**
	 * The Class AnswersStore.
	 */
	private class AnswersStore {

		/** The right answers. */
		// ansswers for depth
		private int[] rightAnswers;

		/** The wrong answers. */
		private int[] wrongAnswers;

		/**
		 * Instantiates a new answers store.
		 *
		 * @param initDepth
		 *            the init depth
		 */
		private AnswersStore(int initDepth) {
			rightAnswers = new int[initDepth];
			wrongAnswers = new int[initDepth];
		}

		/**
		 * Register.
		 *
		 * @param depth
		 *            the depth
		 * @param solution
		 *            the solution
		 */
		private void register(int depth, DepthCertBase.Solution solution) {
			logger.debug("registering " + solution + " at depth " + depth);
			assert depth > 0 && depth <= rightAnswers.length;
			assert solution != DepthCertBase.Solution.NULL;
			if (solution == DepthCertBase.Solution.RIGHT) {
				rightAnswers[depth - 1]++;
				assert rightAnswers[depth - 1] <= RIGHT_ANSWERS_TO_CERTIFY;
			} else {
				wrongAnswers[depth - 1]++;
				assert wrongAnswers[depth - 1] <= WRONG_ANSWERS_TO_STOP_DEPTH;
			}

		}

		/**
		 * Gets the answers.
		 *
		 * @param depth
		 *            the depth
		 * @param solution
		 *            the solution
		 * @return the answers
		 */
		private int getAnswers(int depth, DepthCertBase.Solution solution) {
			assert solution != DepthCertBase.Solution.NULL;
			if (solution == DepthCertBase.Solution.RIGHT) {
				return rightAnswers[depth - 1];
			} else {
				return wrongAnswers[depth - 1];
			}
		}
	}

}
