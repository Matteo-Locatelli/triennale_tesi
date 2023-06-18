package p3d4amb.sat.lib.session;

import static p3d4amb.sat.lib.session.TestSession.Result.CONTINUE;
import static p3d4amb.sat.lib.session.TestSession.Result.FINISH_CERTIFIED;
import static p3d4amb.sat.lib.session.TestSession.Result.FINISH_NOT_CERTIFIED;

import org.apache.log4j.Logger;

/**
 * The score is computed as the best 3 right guesses. It keeps asking a new
 * shape till there is still room to improve the final score.
 *
 * @author garganti
 */
public class Best3DepthCertifierOLD extends DepthCertBase {

	/** The Constant RIGHT_ANSWERS_TO_CERTIFY. */
	private static final int RIGHT_ANSWERS_TO_CERTIFY = 2;

	/** The Constant RIGHT_ANSWERS_TO_CERTIFY. */
	private static final int ANSWERS_TO_CERTIFY = 2;

	/** The Constant WRONG_ANSWERS_TO_STOP_DEPTH. */
	private static final int WRONG_ANSWERS_TO_STOP_DEPTH = 2;

	/**
	 * maxDepth - utilizzata per stabilire il massimo a cui risalire - uguale ad
	 * init Depth per questo certifier
	 */
	private int maxDepth;

	/** The ansstore. */
	// register all the asnwers for depth
	private AnswersStore ansstore;
	/** The logger. */
	private final Logger logger = Logger.getLogger(Best3DepthCertifierOLD.class);

	/** The target depth. */
	// target
	private int targetDepth = 1;

	public Best3DepthCertifierOLD(int initDepth) {
		certifierStatus = new CertifierStatus();
		if (initDepth >= 1)
			certifierStatus.currentDepth = initDepth;
		else
			throw new IllegalArgumentException();
		maxDepth = initDepth;
		ansstore = new AnswersStore(maxDepth);
		certifierStatus.currentResult = TestSession.Result.CONTINUE;
		logger.debug("creating Best3Depth certifier with init " + initDepth + " and max depth " + maxDepth);
	}


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

					if ((ansstore.getAnswers(certifierStatus.currentDepth) <= ANSWERS_TO_CERTIFY)
							& (ansstore.getAnswers(certifierStatus.currentDepth - 1) == 0)) {
						certifierStatus.currentDepth--;
						certifierStatus.currentResult = CONTINUE;
					} else
						certifierStatus.currentResult = FINISH_CERTIFIED;
					// certifierStatus.currentDepth--;
				} else {
					// in target
					assert certifierStatus.currentDepth == targetDepth;
					// in targetDepth
					if (ansstore.getAnswers(certifierStatus.currentDepth) == RIGHT_ANSWERS_TO_CERTIFY) { //Silvia: wrong???
					//if (ansstore.getAnswers(certifierStatus.currentDepth,  DepthCertBase.Solution.RIGHT) == RIGHT_ANSWERS_TO_CERTIFY) {
						certifierStatus.currentResult = FINISH_CERTIFIED;
					} else {
						certifierStatus.currentResult = CONTINUE;
					}

				}
			} else {
				assert (solution == DepthCertBase.Solution.WRONG);

				if (certifierStatus.currentDepth == targetDepth) {
					if ((ansstore.getAnswers(certifierStatus.currentDepth) == ANSWERS_TO_CERTIFY)
							& (ansstore.getAnswers(certifierStatus.currentDepth, DepthCertBase.Solution.RIGHT) == 1))
						certifierStatus.currentResult = FINISH_CERTIFIED;
					else {
						if (ansstore.getAnswers(certifierStatus.currentDepth, DepthCertBase.Solution.WRONG) == 1)
							certifierStatus.currentResult = CONTINUE;
						else {
							if (ansstore.getAnswers(certifierStatus.currentDepth, DepthCertBase.Solution.WRONG) == 2
									& ansstore.getAnswers(certifierStatus.currentDepth + 1) == 2)
								certifierStatus.currentResult = FINISH_CERTIFIED;
							else {
								certifierStatus.currentDepth = certifierStatus.currentDepth + 1;
								certifierStatus.currentResult = CONTINUE;
							}
						}
					}
				} else {
					if (ansstore.getAnswers(certifierStatus.currentDepth) == 1)
						certifierStatus.currentResult = CONTINUE;
					else if (certifierStatus.currentDepth == maxDepth
							& ansstore.getAnswers(certifierStatus.currentDepth) == 2)
						certifierStatus.currentResult = FINISH_NOT_CERTIFIED;
					else if (ansstore.getAnswers(certifierStatus.currentDepth, DepthCertBase.Solution.RIGHT) == 1)
						certifierStatus.currentResult = FINISH_CERTIFIED;
					else {
						if (ansstore.getAnswers(certifierStatus.currentDepth + 1) == 2
								& ansstore.getAnswers(certifierStatus.currentDepth) == 2)
							certifierStatus.currentResult = FINISH_CERTIFIED;
						else {
							certifierStatus.currentResult = CONTINUE;
							certifierStatus.currentDepth = certifierStatus.currentDepth + 1;
						}
					}
				}
			}
		}

	}

	@Override
	public CertifierStatus getCurrentStatus() {
		// TODO
		return certifierStatus;
	}

	/**
	 * The Class AnswersStore.
	 */
	class AnswersStore {

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
		AnswersStore(int initDepth) {
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
				// assert rightAnswers[depth - 1] <= RIGHT_ANSWERS_TO_CERTIFY;
			} else {
				wrongAnswers[depth - 1]++;
				// assert wrongAnswers[depth - 1] <=
				// WRONG_ANSWERS_TO_STOP_DEPTH;
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

		private int getAnswers(int depth) {
			return rightAnswers[depth - 1] + wrongAnswers[depth - 1];
		}

	}

}
